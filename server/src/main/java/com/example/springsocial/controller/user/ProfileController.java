package com.example.springsocial.controller.user;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.dto.ProfileDto;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.AuthService;
import com.example.springsocial.service.GoogleCloudService;
import com.example.springsocial.service.ImageService;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.util.ProjectUtil;
import com.google.cloud.storage.Blob;

@RestController
public class ProfileController {
    

  @Autowired
  private UserService userService;

  @Autowired
  private ProfileService profileService;

 @Autowired
  private GoogleCloudService googleCloudService;

  @Autowired
  private ImageService imageService;
    

  @PostMapping("/api/v1/auth/register/profile")
  public ResponseEntity<String> createProfile(@RequestBody ProfileDto profileDto,Principal principal
  ) throws UsernameNotFoundException {

    //users get a profile once they creat a new user, we're just updating the existed profile here
    Profile profile =  getUserFromPrincipal(principal).getUserProfile();

     profile.setBirthDate(profileDto.getBirthDate());
     profile.setCity(profileDto.getCity());
     if(profileDto.getCountry()!=null){
          profile.setProfileCountry(profileDto.getCountry());

     }
     profile.setFirstName(profileDto.getFirstName());
     profile.setLastName(profileDto.getLastName());
     profile.setSummary(profileDto.getSummary());
     profileService.createNewProfile(profile);
     userService.markeProfileUpdated(profile.getUser());
    return ResponseEntity.ok().body("profile updated successefully") ;
  }


  @DeleteMapping("/api/v1/profile/picture")
  public boolean removeProfilePicture(Principal principal)
  {
    Profile profile = getUserFromPrincipal(principal).getUserProfile();

    if (profile !=null) {
      Image oldImage = profile.getProfilePicture();
      if(oldImage!=null){
        profile.setProfilePicture(null);  
       //remove from DB
       imageService.deletImage(oldImage);
       // remove from google storage cloud // ignore if image from 3rd party
       if (!oldImage.getFileName().equals("oauth2-image")) {
          googleCloudService.deleteFile(oldImage.getFileName()); 
       }
       profileService.createNewProfile(profile);
       userService.markeProfileUpdated(profile.getUser());
       return true;
      }
    }
    return false;

  }

    @PostMapping("/api/v1/auth/register/profile/picture")
    public ResponseEntity<String> udateProfilePic(@RequestParam MultipartFile file,Principal principal) throws Exception{
    Profile profile = getUserFromPrincipal(principal).getUserProfile();    
    Image profileImage = new Image();
    if (profile !=null) {
    /// remove old image if exist
    removeProfilePicture(principal);
    // upload and update new image
    Blob googleCloudFile =  googleCloudService.uploadFile(file, true);
    profileImage.setFileName(googleCloudFile.getName());
    profileImage.setUrl(googleCloudFile.getMediaLink());
    profile.setProfilePicture(profileImage);
    profileService.createNewProfile(profile);
    userService.markeProfileUpdated(profile.getUser());
    return ResponseEntity.ok().body("photo uploaded successefully, Url: "+ profileImage.getUrl());
      }
    else{
          return ResponseEntity.badRequest().body("failed to upload the picture");
    }
    }


    //helper
    private User getUserFromPrincipal(Principal principal){

       Optional<User> user = userService.findByEmail(principal.getName());
       if (!user.isPresent()) {
         throw new UsernameNotFoundException("this user doesnt exist");
        }
        return user.get();
    }



}
