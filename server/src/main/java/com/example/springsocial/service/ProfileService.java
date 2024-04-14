package com.example.springsocial.service;

import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.repository.ProfileRepo;
import com.example.springsocial.repository.UserRepo;

import java.util.Optional;

import com.example.springsocial.util.ProjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ProfileService {

  @Autowired
  private ProfileRepo profileRepo;
  @Autowired
  private UserRepo userRepo;
  private final FireBaseService fireBaseService;
  private final ImageService imageService;


  public Profile createNewProfile(Profile profile) {
    return profileRepo.save(profile);
  }


  public void deleteProfile(Profile profile) {
    profileRepo.delete(profile);
  }

  @Transactional
  public Image changeProfilePicture(Profile profile, MultipartFile file){
    Image imgProfile = new Image();
    String name= fireBaseService.upload(file,true);
    imgProfile.setFileName(name);
    imgProfile.setUrl(ProjectUtil.getMediaUrl(name));
    imageService.save(imgProfile);
   return imgProfile;
  }

 }

