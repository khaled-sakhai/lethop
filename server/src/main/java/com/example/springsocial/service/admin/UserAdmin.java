package com.example.springsocial.service.admin;

import com.example.springsocial.dto.user.RegisterDto;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.repository.*;
import com.example.springsocial.security.Token.Token;
import com.example.springsocial.security.Token.TokenRepo;
import com.example.springsocial.service.FireBaseService;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.specification.UserSpecification;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserAdmin {
    private final UserRepo userRepo;
    private final PostAdmin postAdmin;
    private final PostRepo postRepo;
    private final TokenRepo tokenRepo;
    private final ImageRepo imageRepo;
    private final ProfileRepo profileRepo;
    private CommentRepo commentRepo;
    private ReportRepo reportRepo;
    private ComRepAdmin comRepAdmin;
    private  ProfileAdmin profileAdmin;
    private final   PasswordEncoder passwordEncoder;
    private final ProfileService profileService;
    private final UserService userService;
    private final RoleRepo roleRepo;
    private final FireBaseService fireBaseService;
    private final ImageAdmin imageAdmin;



    public Page<User> findAll(Boolean isActive,String provider,Boolean needProfileUpdate,Long userId,String role,int pageNo, int pageSize, String sortBy, String sortDirection){
        Specification<User> spec=this.userSpec(isActive,provider,needProfileUpdate,userId,role);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);

        return userRepo.findAll(spec,paging);
    }
    public Optional<User> findAnyUserById(long userId){
        return userRepo.adminFindById(userId);
    }

    public Optional<User> findAnyUserByEmail(String email){
        return userRepo.findAnyUserByEmail(email);
    }

    public Page<User> findDeleted(int pageNo,int pageSize,String sortBy,String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return userRepo.adminFindAllDeleted(paging);
    }

    @Transactional
        public void finalDeleteUser(User user) throws IOException {
        user.getUserInterests().clear();
        user.getUserReplies().clear();
        Profile userProfile = user.getUserProfile();
        user.setUserProfile(null);
        profileAdmin.deleteProfile(userProfile);

        // Make a copy of the user's comments, posts, and tokens to iterate over
        List<Comment> commentsCopy = new ArrayList<>(user.getUserComments());
        List<Post> postsCopy = new ArrayList<>(user.getPosts());
        List<Token> tokensCopy = new ArrayList<>(user.getTokens());

        // Clear the original collections
        user.getUserComments().clear();
        user.getPosts().clear();
        user.getTokens().clear();

        // Iterate over copies and perform deletion
        commentsCopy.forEach(c -> comRepAdmin.deleteComment(c));
        postsCopy.forEach(postAdmin::finalDeletePost);
        tokensCopy.forEach(t -> {
            t.setUser(null);
            tokenRepo.adminDeleteById(t.getId());
        });

        // Clear remaining collections
        user.getLikedPosts().clear();
        user.getSavedPosts().clear();
        user.getRoles().clear();

        // Finally, delete the user
        userRepo.adminDeleteById(user.getId());
    }

    public void adminAddNewUser(RegisterDto registerDto){
        User user = new User();
        user.setActive(true);
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setProvider(AuthProvider.local);
        Profile profile = new Profile();
        profile.setUser(user);
        user.setUserProfile(profile);
        profileService.createNewProfile(profile);
        userService.addUser(user);
    }

    public void adminEditUserRoles(User user,List<String> newRoles){
        // roles
        if(!newRoles.isEmpty()){
            Set<Role> roles= user.getRoles();
            roles.clear();
            newRoles.forEach(roleString->{
                Role role= roleRepo.findByName(APPRole.valueOf(roleString));
                roles.add(role);
            });
        }
        userRepo.save(user);
    }
    private Specification<User> userSpec(Boolean isActive,String provider,Boolean needProfileUpdate,Long userId,String role){
        return Specification.where(UserSpecification.byActive(isActive)
                        .and(UserSpecification.byProvider(provider))
                        .and(UserSpecification.byProfileUpdated(needProfileUpdate))
                        .and(UserSpecification.byUserId(userId))
                        .and(UserSpecification.byRole(role))
                );
    }
}
