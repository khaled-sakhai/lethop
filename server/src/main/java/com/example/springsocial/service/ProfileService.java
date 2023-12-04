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


  public Profile createNewProfile(Profile profile) {
    return profileRepo.save(profile);
  }

  public Optional<Profile> FindById(Long id){
   return profileRepo.findById( id);
   
  }

 }

