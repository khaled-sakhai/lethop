package com.example.springsocial.dto;

import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.enums.NotificationType;
import com.example.springsocial.util.ProjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificationDto implements Serializable {

    private String type;
    private UserInfo fromUser;
    private String date;
    private boolean isRead;
    private Long postId;
    private Long commentId;
    private Long replyId;

    public NotificationDto(Notification notification){

        this.type=notification.getType().toString();

        this.fromUser=new UserInfo(notification.getFromUser());

        this.date= ProjectUtil.convertDateToString(notification.getCreatedDate());

        this.isRead=notification.isRead();


        if (notification.getType().equals(NotificationType.POST_LIKE)){
            this.postId=notification.getRelatedPost().getId();
        }

        else if (notification.getType().equals(NotificationType.COMMENT)){
            this.postId=notification.getRelatedPost().getId();
            this.commentId=notification.getRelatedComment().getId();
        }


        else if (notification.getType().equals(NotificationType.COMMENT_LIKE)){
            this.postId=notification.getRelatedPost().getId();
            this.commentId=notification.getRelatedComment().getId();
        }

        else if (notification.getType().equals(NotificationType.REPLY)){
            this.postId=notification.getRelatedPost().getId();
            this.commentId=notification.getRelatedComment().getId();
            this.replyId=notification.getReply().getId();
        }

        else if (notification.getType().equals(NotificationType.REPLAY_LIKE)){
            this.postId=notification.getRelatedPost().getId();
            this.commentId=notification.getRelatedComment().getId();
            this.replyId=notification.getReply().getId();
        }
    }





}
