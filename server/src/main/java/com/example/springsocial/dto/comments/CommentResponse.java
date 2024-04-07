package com.example.springsocial.dto.comments;

import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.util.ProjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse implements Serializable {

    private String content;
    private String lastModifiedDate;
    private UserInfo userInfo;

    public CommentResponse(Comment comment){
      User user = comment.getUser();
      this.content=comment.getContent();
      this.lastModifiedDate= ProjectUtil.convertDateToString(comment.getLastModifiedDate());
      userInfo = new UserInfo(user);
    }

}
