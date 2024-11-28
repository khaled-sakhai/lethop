package com.example.springsocial.security.oauth2;

import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.exception.OAuth2AuthenticationProcessingException;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.security.Token.JwtService;
import com.example.springsocial.security.Token.Token;
import com.example.springsocial.security.Token.TokenService;
import com.example.springsocial.security.oauth2.user.OAuth2UserInfo;
import com.example.springsocial.security.oauth2.user.OAuth2UserInfoFactory;
import com.example.springsocial.service.ImageService;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    private final ProfileService profileService;

    private final RoleRepo roleRepo;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());


        if(oAuth2UserInfo.getEmail().isEmpty()){
                        throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        Optional<User> userOptional = userService.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
               
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        String email=oAuth2UserInfo.getEmail();
        String emailWithoutProvider= email.substring(0, email.indexOf("@")).replace(".","-");
        User user = new User();
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setEmail(email);
       /// user.setImageUrl(oAuth2UserInfo.getImageUrl());

       /// since the user signed up with a social media -- no need for extra email verification
        user.setActive(true);
        user.setActivationDate(new Date());
        System.out.printf("user name: ===>"+ emailWithoutProvider);
        if (!userService.isUserNameTaken(emailWithoutProvider)) {
            user.setUsername(emailWithoutProvider);
        }
         else {
            int randomDigit = (int) (Math.random() * 10);
            user.setUsername(emailWithoutProvider+randomDigit);
        }

        Profile profile = new Profile();
        Image image = new Image();
        image.setUrl(oAuth2UserInfo.getImageUrl());
        image.setFileName("oauth2-image");
        profile.setProfilePicture(image);

        profile.setUser(user);
        user.setUserProfile(profile);
        profileService.createNewProfile(profile);
        user.addRoles(roleRepo.findByName(APPRole.ROLE_USER));
        return userService.updateUser(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        /// update existed user--- nothing to update for now
        return userService.updateUser(existingUser);
    }

}
