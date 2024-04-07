package com.example.springsocial.service.admin;

import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.*;
import com.example.springsocial.security.Token.TokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProfileAdmin {

    private final UserRepo userRepo;
    private final PostAdmin postAdmin;
    private final PostRepo postRepo;
    private final TokenRepo tokenRepo;
    private final ImageRepo imageRepo;
    private final ProfileRepo profileRepo;
    private CommentRepo commentRepo;
    private ReportRepo reportRepo;
    private ComRepAdmin comRepAdmin;



    @Transactional
    public void deleteProfile(Profile profile){
        if (profile!= null) {
            profile.setUser(null);
            if (profile.getProfilePicture() != null) {
                Image profilePicture = profile.getProfilePicture();
                profile.setProfilePicture(null);
                imageRepo.deleteById(profilePicture.getId());
            }
            profileRepo.adminDeleteById(profile.getId());
        }
    }



}
