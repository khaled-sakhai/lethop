package com.example.springsocial.dto.user;

import com.example.springsocial.dto.profile.ProfileResponse;
import com.example.springsocial.entity.userRelated.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private boolean isActive;
    private String provider;
    private String email;
    private boolean needProfileUpdate;// means user has never updated profile;
    private ProfileResponse profile;
    private List<String> roles=new ArrayList<>();
    private int savedCount;
    private int likedCount;
    private int commentCount;
    private List<String> interests=new ArrayList<>();

    public UserDto(User user){
        this.isActive=user.isActive();
        this.provider=user.getProvider().name();
        this.needProfileUpdate=user.isNeedProfileUpdate();
        this.email=user.getEmail();
        this.savedCount=user.getSavedPostsCount();
        this.likedCount=user.getLikedPostsCount();
        this.commentCount=user.getCommentPostsCount();
        user.getUserInterests().forEach(i->this.interests.add(i.getTagName()));
        user.getRoles().forEach(r-> this.roles.add(r.getName().name()));
        this.profile=new ProfileResponse(user);

    }

}
