package com.example.springsocial.dto.user;

import com.example.springsocial.entity.userRelated.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {

    private Long userId;
    private String userName;
    private String userImageUrl;

    public  UserInfo(User user){
        this.userId = user.getId();
        this.userName=user.getUserProfile().getFullName();
        this.userImageUrl=user.getUserProfile().getProfilePicture().getUrl();
    }
}
