package com.example.springsocial.service;

import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.entity.Features.UserVerificationCode;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.VerificationType;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.repository.TagRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.repository.UserVerificationCodeRepo;
import com.example.springsocial.service.emailService.EmailSenderService;
import com.example.springsocial.util.EmailTemplates;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private RoleRepo roleRepo;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  private ProfileService profileService;

  @Autowired
  private UserVerificationCodeRepo userVerificationCodeRepo;

  @Autowired 
  private UserVerificationCodeService userVerificationCodeService;
  @Autowired
  private EmailSenderService emailSenderService;

  private final TagRepo tagRepo;

  public void changePreferences(User user,List<String> tags){
    user.getUserInterests().clear();
    for (String tagString:tags){
      Optional<Tag> tag= tagRepo.findByTagName(tagString);
        tag.ifPresent(value -> user.getUserInterests().add(value));
    }
  }

  public Optional<User> findByEmail(String email) {
    return userRepo.findByEmail(email);
  }

  public boolean isUserNameTaken(String username) {return userRepo.existsByUsername(username);}

  public Optional<User> findById(Long id){
    return userRepo.findById(id);
  }

  public boolean isEmailTaken(String email){
    return userRepo.existsByEmail(email);
  }

  public User addUser(User user)  {
    Role role=roleRepo.findByName(APPRole.ROLE_USER);
    user.addRoles(role);
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



    public boolean userEmailVerification(String verificationCode) throws Exception{
      UserVerificationCode userVerificationCode = validateVerificationCode(verificationCode);

      if (userVerificationCode.getType().equals(VerificationType.SIGNUP) && userVerificationCode.isConfirmed()) {
        User user = userVerificationCode.getUser();
        user.setActive(true);
        user.setActivationDate(userVerificationCode.getLastModifiedDate());
        this.updateUser(user);
        userVerificationCodeRepo.deleteById(userVerificationCode.getId());
        return true;
      }
      return false;
    }

   
    public boolean userPasswordResetVerification(String verificationCode,String newPassword) throws Exception{
      UserVerificationCode userVerificationCode = userVerificationCodeRepo.findByConfirmationCode(verificationCode).orElseThrow();
      if (userVerificationCode.getType().equals(VerificationType.PASSWORD) && userVerificationCode.isConfirmed()) {
        User user = userVerificationCode.getUser();
        this.changePassword(newPassword, user);
        userVerificationCodeRepo.delete(userVerificationCode);
        return true;
      }
      else{
       throw new Exception("Confirmation code is invalid!");
      }
    }



  public void confirmationCodeSend(User user, VerificationType type){
     UserVerificationCode userVerificationCode = new UserVerificationCode(user,type);
     userVerificationCodeRepo.save(userVerificationCode);
     SimpleMailMessage mailMessage = new SimpleMailMessage();
     if(type== VerificationType.PASSWORD){
      mailMessage=EmailTemplates.passwordResetEmail(user.getUserProfile().getFullName(), user.getEmail(), userVerificationCode.getConfirmationCode());
     }
     if(type== VerificationType.SIGNUP){
      mailMessage=EmailTemplates.newUserEmail(user.getUserProfile().getFullName(), user.getEmail(), userVerificationCode.getConfirmationCode());
     }
      emailSenderService.sendEmail(mailMessage);
  }
 


  public UserVerificationCode validateVerificationCode(String verificationCode) throws Exception{
    UserVerificationCode userVerificationCode = userVerificationCodeRepo.findByConfirmationCode(verificationCode).orElseThrow();
    if(userVerificationCode.isConfirmed()){
      throw new Exception("Verification code invalid!");
    }
    userVerificationCode.setConfirmed(true);;
    return userVerificationCodeRepo.save(userVerificationCode);
  }


}
