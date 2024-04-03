package com.example.springsocial.controller.user;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.example.springsocial.dto.profile.PreferencesDto;
import com.example.springsocial.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.dto.profile.ProfileDto;
import com.example.springsocial.dto.profile.ProfileResponse;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.util.ProjectUtil;

@RestController
@AllArgsConstructor
public class ProfileController {
    

  @Autowired
  private UserService userService;

  @Autowired
  private ProfileService profileService;

 private final FireBaseService fireBaseService;

  @Autowired
  private ImageService imageService;
    

  @PutMapping("/api/v1/auth/register/profile")
  public ResponseEntity<String> updateProfile(@RequestBody @Valid ProfileDto profileDto,Principal principal
  ) throws UsernameNotFoundException {

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
    return ResponseEntity.ok().body("profile updated successfully") ;
  }

  @DeleteMapping("/api/v1/profile/picture")
  public boolean removeProfilePicture(Principal principal) throws IOException {
    Profile profile = getUserFromPrincipal(principal).getUserProfile();

    if (profile !=null) {
      Image oldImage = profile.getProfilePicture();
      if(oldImage!=null){
        profile.setProfilePicture(null);  
       //remove from DB
       imageService.deletImage(oldImage);
       // remove from google storage cloud // ignore if image from 3rd party
       if (!oldImage.getFileName().equals("oauth2-image")) {
          fireBaseService.deleteFile(oldImage.getFileName());
       }
       profileService.createNewProfile(profile);
       userService.markeProfileUpdated(profile.getUser());
       return true;
      }
    }
    return false;

  }

    @PostMapping("/api/v1/auth/register/profile/picture")
    public ResponseEntity<String> updateProfilePic(@RequestParam(required = true) MultipartFile file, Principal principal) throws Exception{
    Profile profile = getUserFromPrincipal(principal).getUserProfile();    
    Image profileImage = new Image();
    if (profile !=null) {
    /// remove old image if exist
    removeProfilePicture(principal);
    // upload and update new image
    String picName=  fireBaseService.upload(file, true);
    profileImage.setFileName(picName);
    profileImage.setUrl(ProjectUtil.getMediaUrl(picName));
    profile.setProfilePicture(profileImage);
    profileService.createNewProfile(profile);
    userService.markeProfileUpdated(profile.getUser());
    return ResponseEntity.ok().body("photo uploaded successfully, Url: "+ profileImage.getUrl());
      }
    else{
          return ResponseEntity.badRequest().body("failed to upload the picture");
    }
    }

    @PostMapping(path = "api/v1/user/preferences")
    public ResponseEntity<String> changePreferences(@RequestBody PreferencesDto preferencesDto,Principal principal){
      User user= userService.findByEmail(principal.getName()).orElseThrow();
      List<String> tagList = Arrays.asList(preferencesDto.getTags().split(","));
      userService.changePreferences(user,tagList);
      return ResponseEntity.status(HttpStatus.OK).body("User preferences has been sat!");
    }

    @GetMapping("api/v1/public/user/{userid}")
    public ResponseEntity<ProfileResponse> getUserProfile(@PathVariable Long userid){
      Optional<User> user = userService.findById(userid);
      if(user.isPresent()){
        return ResponseEntity.status(HttpStatus.OK).body(new ProfileResponse(user.get()));
      }
      else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
