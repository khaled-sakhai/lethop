package com.example.springsocial.dto.comments;

import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.util.ProjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private String content;
    private String lastModifiedDate;

    private UserInfo userInfo;

    public CommentResponse(Comment comment){
      User user = comment.getUser();
      this.content=comment.getContent();
      this.lastModifiedDate= ProjectUtil.convertDateToString(comment.getLastModifiedDate());
      userInfo = new UserInfo();
      userInfo.setUserId(user.getId());
      userInfo.setUserName(user.getUserProfile().getFullName());
      userInfo.setUserImageUrl(user.getUserProfile().getProfilePicture().getUrl());
    }

}
