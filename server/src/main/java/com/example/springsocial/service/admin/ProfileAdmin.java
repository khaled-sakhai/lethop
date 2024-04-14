package com.example.springsocial.service.admin;

import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.repository.*;
import com.example.springsocial.security.Token.TokenRepo;
import com.example.springsocial.service.FireBaseService;
import com.example.springsocial.specification.ProfileSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileAdmin {

    private final UserRepo userRepo;
    private final PostAdmin postAdmin;
    private final PostRepo postRepo;
    private final TokenRepo tokenRepo;
    private final ImageRepo imageRepo;
    private final ProfileRepo profileRepo;
    private final ImageAdmin imageAdmin;
    private CommentRepo commentRepo;
    private ReportRepo reportRepo;
    private ComRepAdmin comRepAdmin;
    private final FireBaseService fireBaseService;



    public Page<Profile> getAll(Long profileId,Long userId, String country, String city, String firstName, String lastName, Integer year, Integer month, Integer day, int pageNo, int pageSize, String sortBy, String sortDirection){
        Specification<Profile> spec=this.profileSpecification(profileId,userId,country,city,firstName,lastName,year,month,day);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return profileRepo.findAll(spec,paging);
    }
    public Optional<Profile> findAnyProfileById(Long profileId){
        return profileRepo.adminFindById(profileId);
    }

    public void adminDeleteProfilePicture(Image image,Profile profile) throws IOException {
        imageAdmin.adminDeleteImage(image);
    }
    public Page<Profile> findAllDeleted(int pageNo, int pageSize, String sortBy, String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return profileRepo.adminFindAllDeleted(paging);
    }
    public void editProfile(Profile profile){
        profileRepo.save(profile);
    }




    @Transactional
    public void deleteProfile(Profile userProfile) throws IOException {
        if (userProfile != null) {
            // Dissociate profile picture from profile
            userProfile.setUser(null);
            Image oldProfilePicture = userProfile.getProfilePicture();
            if (oldProfilePicture != null) {
                userProfile.setProfilePicture(null);
                profileRepo.save(userProfile); // Update profile to remove the profile picture relationship
            }

            // Delete the profile picture if it's not referenced by any other profile
            if (oldProfilePicture != null ) {
                imageAdmin.adminDeleteImage(oldProfilePicture);
            }

            profileRepo.adminDeleteById(userProfile.getId());
        }
    }

    private Specification<Profile> profileSpecification(Long profileId,Long userId,String country,String city,String firstName,String lastName,Integer year,Integer month,Integer day){
        return Specification.where(
                ProfileSpecification.byProfileId(profileId)
                .and(ProfileSpecification.byCountry(country))
                .and(ProfileSpecification.byBirthDate(year,month,day))
                .and(ProfileSpecification.byCity(city))
                .and(ProfileSpecification.byFirstName(firstName))
                .and(ProfileSpecification.byLastName(lastName))
                .and(ProfileSpecification.byUserId(userId)));
    }

}
