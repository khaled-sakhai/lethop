package com.example.springsocial.dto.user;

import com.example.springsocial.dto.ImageDto;
import com.example.springsocial.entity.userRelated.User;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo implements Serializable {

    private Long userId;
    private String userName;
    private ImageDto userPhoto;

    public  UserInfo(User user){
        if(user!=null){
            this.userId = user.getId();
            this.userName=user.getUserProfile().getFullName();
            if (user.getUserProfile().getProfilePicture()!=null){
                this.userPhoto=new ImageDto(user.getUserProfile().getProfilePicture());
            }
        }

    }
}
