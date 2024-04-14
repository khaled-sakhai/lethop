package com.example.springsocial.controller.admin;

import com.example.springsocial.dto.profile.ProfileDto;
import com.example.springsocial.dto.profile.ProfileResponse;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.admin.ImageAdmin;
import com.example.springsocial.service.admin.ProfileAdmin;
import com.example.springsocial.util.PathConstants;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = PathConstants.API_V1+PathConstants.ADMIN_END_POINT)

public class ProfileAdminController {
    
    private final ProfileAdmin profileAdmin;
    private final ImageAdmin imageAdmin;
    private final  ProfileService profileService;


    @GetMapping(value = "Profile")
    public ResponseEntity<Page<ProfileResponse>> getAllProfiles(@RequestParam(required = false) Long profileId,
                                                                @RequestParam(required = false) Long userId,
                                                                @RequestParam(required = false)  String country,
                                                                @RequestParam(required = false)  String city,
                                                                @RequestParam (required = false) String firstName,
                                                                @RequestParam(required = false)  String lastName,
                                                                @RequestParam(required = false)  Integer year,
                                                                @RequestParam(required = false)  Integer month,
                                                                @RequestParam(required = false)  Integer day,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                                                @RequestParam(defaultValue = "20") int size,
                                                                @RequestParam(defaultValue = "desc") String sortDirection){
        
        Page<Profile> ProfilePage = profileAdmin.getAll(profileId,userId,country,city,firstName,lastName,year,month,day,page, size, sortBy, sortDirection);
        if (!ProfilePage.isEmpty()) {
            Page<ProfileResponse> ProfileDtos= ProfilePage.map(ProfileResponse::new);
            return new ResponseEntity<>(ProfileDtos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "Profile/deleted")
    public ResponseEntity<Page<ProfileResponse>> getAllDeletedProfiles(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                                              @RequestParam(defaultValue = "20") int size,
                                                              @RequestParam(defaultValue = "desc") String sortDirection){
        Page<Profile> ProfilePage = profileAdmin.findAllDeleted(page, size, sortBy, sortDirection);
        if (!ProfilePage.isEmpty()) {
            Page<ProfileResponse> ProfileDtos= ProfilePage.map(ProfileResponse::new);
            return new ResponseEntity<>(ProfileDtos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping(value = "Profile/{ProfileId}")
    public ResponseEntity<Profile> getAnyProfileById(@PathVariable Long ProfileId){
        Optional<Profile> ProfileOptional = profileAdmin.findAnyProfileById(ProfileId);
        if (ProfileOptional.isPresent()) {
            return new ResponseEntity<>(ProfileOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    @PutMapping(value = "profile/{profileId}")
    public ResponseEntity<String> editProfile(@PathVariable Long profileId,
                                              @RequestPart(required = false) ProfileDto profileDto,
                                              @RequestPart(required = false) MultipartFile profilePicture) throws IOException {
        Optional<Profile> profileOptional = profileAdmin.findAnyProfileById(profileId);
        if (profileOptional.isPresent()){
            Profile profile = profileOptional.get();
            if (profileDto!=null){
                profile.setProfileCountry(profileDto.getCountry());
                profile.setCity(profile.getCity());
                profile.setSummary(profile.getSummary());
                profile.setBirthDate(profileDto.getBirthDate());
                profile.setFirstName(profile.getFirstName());
                profile.setLastName(profile.getLastName());
                profile.setNotificationEmailed(profile.isNotificationEmailed());
                profile.setPostEmailed(profile.isPostEmailed());
            }

            if (profilePicture != null && !profilePicture.isEmpty()) {
                // Delete the old profile picture if it exists
                Image oldProfilePicture = profile.getProfilePicture();
                profile.setProfilePicture(null);
                // Create a new profile picture
                Image newProfilePicture = profileService.changeProfilePicture(profile, profilePicture);

                if (oldProfilePicture != null) {
                    profileAdmin.adminDeleteProfilePicture(oldProfilePicture, profile);
                }
                // Set the new profile picture
                profile.setProfilePicture(newProfilePicture);
            }

            // Update the profile
            profileService.createNewProfile(profile);
            return ResponseEntity.ok().body("Profile has been updated successfully!");

        }
        return ResponseEntity.badRequest().body("Failed to update profile!");
    }

    @DeleteMapping(path = "Profile/{ProfileId}")
    @Transactional
    public ResponseEntity<String> removeProfile(@PathVariable long ProfileId,@RequestParam(required = false) boolean finalDelete) throws IOException {
        Optional<Profile> ProfileOptional = profileAdmin.findAnyProfileById(ProfileId);
        if (ProfileOptional.isPresent()) {
            if (finalDelete) {
                profileAdmin.deleteProfile(ProfileOptional.get());
            } else {
                profileService.deleteProfile(ProfileOptional.get());
            }
            return ResponseEntity.ok().body("Profile removed!");
        }
        return  ResponseEntity.badRequest().body("Profile was not removed!");
    }
    
    
}
