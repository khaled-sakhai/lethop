package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.repository.ProfileRepo;
import com.example.springsocial.repository.UserRepo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

  @Autowired
  private ProfileRepo profileRepo;
  @Autowired
  private UserRepo userRepo;


  public Profile createNewProfile(Profile profile) {
    return profileRepo.save(profile);
  }

  public Optional<Profile> FindById(Long id){
   return profileRepo.findById( id);
  }

  public void deleteProfile(Profile profile) {
    profileRepo.delete(profile);
  }

  // public Profile updateProfileImage(Long userId,String imageUrl){
  //   Profile profile = profileRepo.FindProfileByUser(userId);
  //   profile.setProfilePictureRef(imageUrl);
  //   return profileRepo.save(profile);
  // }
 }

