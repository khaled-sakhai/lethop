package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.util.ProjectUtil;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    System.out.println("new password: "+ newPassword);
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





}
