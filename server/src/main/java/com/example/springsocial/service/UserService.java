package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.util.ProjectUtil;

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

  public boolean isEmailTaken(String email){
    return userRepo.existsByEmail(email);
  }

  public User addUser(User user)  {

    return userRepo.save(user);
  }

  public User updateUser(User user) {
    return userRepo.save(user);
  }

  public void deleteUser(User user){
    userRepo.delete(user);
  }




}
