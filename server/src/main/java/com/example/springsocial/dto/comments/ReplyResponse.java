package com.example.springsocial.dto.comments;

import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.util.ProjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponse implements Serializable {
    private Long commentId;
    private String content;
    private String lastModifiedDate;
    private UserInfo userInfo;

    public ReplyResponse(Reply reply){
        User user = reply.getUser();
        this.content=reply.getContent();
        this.commentId= reply.getComment().getId();
        this.lastModifiedDate= ProjectUtil.convertDateToString(reply.getLastModifiedDate());
        userInfo = new UserInfo(reply.getUser());

    }


}
