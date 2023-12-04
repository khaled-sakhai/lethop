// package com.example.springsocial.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.springsocial.dto.ProfileDto;
// import com.example.springsocial.entity.Profile;
// import com.example.springsocial.entity.User;
// import com.example.springsocial.service.AuthService;
// import com.example.springsocial.service.GoogleCloudService;
// import com.example.springsocial.service.ProfileService;
// import com.example.springsocial.service.UserService;
// import com.example.springsocial.util.ProjectUtil;

// @RestController
// public class ProfileController {
    

//   @Autowired
//   private AuthService authService;

//   @Autowired
//   private UserService userService;

//   @Autowired
//   private ProfileService profileService;
//  @Autowired
//   private GoogleCloudService googleCloudService;
    
//   @PostMapping("/api/v1/auth/register/profile")
//   public String createProfile(
//     @RequestParam("reg") String registerationId,
//     @RequestBody ProfileDto profileDto
//   ) throws UsernameNotFoundException {
//     System.out.println(registerationId);
//     User user = userService.findByRegisterId(registerationId).get();
//     //check if user / profile exist
//     if (user == null) {
//       throw new UsernameNotFoundException("this user doesnt exist");
//     }

//    Profile profile = profileService.FindById(user.getUserProfile().getId()).get();
//    System.out.println(profile.getFirstName() + " " + profile.getLastName());
//    System.out.println(profile.getUser().getId());
//     //edit profile:
//     //when user creat a new user, they create a profile automatically.//we just edit here.
//     profile.setCity(profileDto.getCity());
//     profile.setCountry(profileDto.getCountry());
//     profile.setBirthDate(profileDto.getBirthDate());
//     profile.setFirstName(profileDto.getFirstName());
//     profile.setLastName(profileDto.getLastName());
//     profile.setSummary(profileDto.getSummary());
//     //change the unique id - for security perposes.
//     user.setRegisterId(ProjectUtil.generateRandomId());
//     userService.updateUser(user);
//     profileService.createNewProfile(profile);
//     return "Registration completed!";
//   }


//   //Upload profile pic
//   @PostMapping("api/v1/auth/register/profile/picture")
//   public ResponseEntity<String> uploadFile(@RequestParam("reg") String registerationId,@RequestParam MultipartFile file)
//     throws Exception,UsernameNotFoundException {
//     User user = userService.findByRegisterId(registerationId).orElse(null);
//     //check if user / profile exist
//     if (user == null) {
//       throw new UsernameNotFoundException("this user doesnt exist");
//     }
//     Profile profile = profileService.FindById(user.getUserProfile().getId()).get();
//     String pictureRef=googleCloudService.uploadFile(file, true);
//     profile.setProfilePictureRef(pictureRef);
//     profileService.createNewProfile(profile);
//     return ResponseEntity.ok("File uploaded successfully");
//   }

// }
