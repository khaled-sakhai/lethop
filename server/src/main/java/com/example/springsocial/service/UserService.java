package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.util.ProjectUtil;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

  public User addUser(User user) throws Exception {
    if (isEmailTaken(user.getEmail())) {
      throw new Exception("This email is already been used");
    }

    User newUser = new User();
    // create Profile
    Profile profile = new Profile();
    profile.setFirstName("New");
    profile.setLastName("User");
    profile.setUser(newUser);    
    newUser.setUserProfile(profile);
    /////
    /// will implement send regId TO EMAIL to activate user

    // we set it to true for now
    newUser.setActive(true);
    newUser.setEmail(user.getEmail());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    Role roleUser = roleRepo.findByName("ROLE_USER");
    roleRepo.save(roleUser);
    newUser.getRoles().add(roleUser);
    newUser.setLastModifiedBy(user.getEmail());

    profileService.createNewProfile(profile);

    return userRepo.save(newUser);
  }

  public User updateUser(User user) {
    return userRepo.save(user);
  }


}
