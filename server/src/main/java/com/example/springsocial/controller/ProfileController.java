package com.example.springsocial.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.dto.ProfileDto;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.AuthService;
import com.example.springsocial.service.GoogleCloudService;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.util.ProjectUtil;

@RestController
public class ProfileController {
    

  @Autowired
  private UserService userService;

  @Autowired
  private ProfileService profileService;

 @Autowired
  private GoogleCloudService googleCloudService;
    

  @PostMapping("/api/v1/auth/register/profile")
  public ResponseEntity<String> createProfile(@RequestBody ProfileDto profileDto,Principal principal
  ) throws UsernameNotFoundException {

    //users get a profile once they creat a new user, we're just updating the existed profile here
    Profile profile = getProfileFromPrincipal(principal);

     profile.setBirthDate(profileDto.getBirthDate());
     profile.setCity(profileDto.getCity());
     profile.setProfileCountry(profileDto.getCountry());
     profile.setFirstName(profileDto.getFirstName());
     profile.setLastName(profileDto.getLastName());
     profile.setSummary(profileDto.getSummary());
     profileService.createNewProfile(profile);
    return ResponseEntity.ok().body("profile updated successefully") ;
  }



    @PostMapping("/api/v1/auth/register/profile/picture")
    public ResponseEntity<String> udateProfilePic(@RequestParam MultipartFile file,Principal principal) throws Exception{
     
    Profile profile = getProfileFromPrincipal(principal);
    String url =  googleCloudService.uploadFile(file, true);
    profile.setProfilePictureRef(url);
    profileService.createNewProfile(profile);
    return ResponseEntity.ok().body("photo uploaded successefully, Url: "+ url);
    }

    //helper
    private Profile getProfileFromPrincipal(Principal principal){

       Optional<User> user = userService.findByEmail(principal.getName());
       if (!user.isPresent()) {
         throw new UsernameNotFoundException("this user doesnt exist");
        }

        return user.get().getUserProfile();
    }



}
