package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.entity.userRelated.UserVerificationCode;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.enums.VerficicationType;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.repository.UserVerificationCodeRepo;
import com.example.springsocial.service.emailService.EmailSenderService;
import com.example.springsocial.util.EmailTemplates;
import com.example.springsocial.util.ProjectUtil;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class UserService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private RoleRepo roleRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ProfileService profileService;

  @Autowired
  private UserVerificationCodeRepo userVerificationCodeRepo;

  @Autowired 
  private UserVerificationCodeService userVerificationCodeService;
  @Autowired
  private EmailSenderService emailSenderService;

  public Optional<User> findByEmail(String email) {
    return userRepo.findByEmail(email);
  }

  public Optional<User> findById(Long id){
    return userRepo.findById(id);
  }

  public boolean isEmailTaken(String email){
    return userRepo.existsByEmail(email);
  }

  public User addUser(User user)  {

    return userRepo.save(user);
  }

  public User updateUser(User user) {
    return userRepo.save(user);
  }

  public boolean updateEmail(User user,String newEmail){
    if(user.isActive()){
      user.setEmail(newEmail);
     updateUser(user);
     return true;
    }
    return false;   
  }

  public void markeProfileUpdated(User user){
    user.setNeedProfileUpdate(false);
    updateUser(user);
  }

  public void deleteUser(User user){
    userRepo.delete(user);
  }
  
  public boolean checkIfValidOldPassword(String oldPassword,User user){
    return passwordEncoder.matches(oldPassword, user.getPassword());
  }

  public void changePassword(String newPassword,User user){
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepo.save(user);
  }

  public List<User> findAll(){
    return userRepo.findAll();
  }

  public List<User> findAllActiveUsers(AuthProvider provider){
    return userRepo.findByIsActiveTrueAndProvider(provider);
  }

  public List<User> findAllNonActiveUsers(AuthProvider provider){
    return userRepo.findByIsActiveFalseAndProvider(provider);
  }


    public boolean userEmailVerification(String verificationCode){
      UserVerificationCode userVerificationCode = validateVerificationCode(verificationCode);;
      System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
      if (userVerificationCode.getType().equals(VerficicationType.SIGNUP)) {
        User user = userVerificationCode.getUser();
        user.setActive(true);
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");

        this.updateUser(user);
        userVerificationCode.setConfirmed(true);
        userVerificationCodeRepo.save(userVerificationCode);
        //we remove the verification code once the user used it,just to clear space
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");

        return true;
      }
      return false;
    }

    public boolean userPasswordResetVerification(String verificationCode,String newPassword){
      UserVerificationCode userVerificationCode = validateVerificationCode(verificationCode);
      if (userVerificationCode.getType().equals(VerficicationType.PASSWORD)) {
        User user = this.findByEmail(userVerificationCode.getUser().getEmail()).orElseThrow();
        //this.changePassword(newPassword, user);
        //this.updateUser(user);
        //we remove the verification code once the user used it,just to clear space
        //userVerificationCodeService.removeAfterVerify(userVerificationCode);
        return true;
      }
      return false;
    }


    public void resetUserPassword(String newPassword,String verificationCode){
      UserVerificationCode userVerificationCode = validateVerificationCode(verificationCode);
      if (userVerificationCode.getType().equals(VerficicationType.PASSWORD) && userVerificationCode.isConfirmed()){
        User user = this.findByEmail(userVerificationCode.getUser().getEmail()).orElseThrow();
        this.changePassword(newPassword, user);
        this.updateUser(user);
        //we remove the verification code once the user used it,just to clear space
        userVerificationCodeService.removeAfterVerify(userVerificationCode);
      }
    }


  public void confirmationCodeSend(User user,VerficicationType type){
     UserVerificationCode userVerificationCode = new UserVerificationCode(user);
     userVerificationCode.setType(type);
     userVerificationCodeRepo.save(userVerificationCode);
     SimpleMailMessage mailMessage = new SimpleMailMessage();
     if(type==VerficicationType.PASSWORD){
      mailMessage=EmailTemplates.passwordRessetEmail(user.getUserProfile().getFullName(), user.getEmail(), userVerificationCode.getConfirmationCode());
     }
     if(type==VerficicationType.SIGNUP){
      mailMessage=EmailTemplates.newUserEmail(user.getUserProfile().getFullName(), user.getEmail(), userVerificationCode.getConfirmationCode());
     }
      emailSenderService.sendEmail(mailMessage);
  }


  private UserVerificationCode validateVerificationCode(String verificationCode){
    UserVerificationCode userVerificationCode = userVerificationCodeRepo.findByConfirmationCode(verificationCode).orElseThrow();
   return userVerificationCode;
  }


}
