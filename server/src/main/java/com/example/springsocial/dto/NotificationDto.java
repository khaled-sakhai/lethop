package com.example.springsocial.dto;

import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.Notification.Notification;
import com.example.springsocial.enums.NotificationType;
import com.example.springsocial.util.ProjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NotificationDto {

    private String type;
    private UserInfo fromUser;
    private String date;
    private boolean isRead;
    private boolean isDelivered;
    private Long postId;
    private Long commentId;
    private Long replyId;

    public NotificationDto(Notification notification){

        this.type=notification.getType().toString();

        this.fromUser=new UserInfo(notification.getFromUser().getId(),notification.getFromUser().getUserProfile().getFullName(),notification.getFromUser().getUserProfile().getProfilePicture().getUrl());

        this.date= ProjectUtil.convertDateToString(notification.getCreatedDate());

        this.isRead=notification.isRead();

        this.isDelivered=notification.isDelivered();

        if (notification.getType().equals(NotificationType.POST_LIKE)){
            this.postId=notification.getRelatedPost().getId();
        }

        if (notification.getType().equals(NotificationType.COMMENT)){
            this.postId=notification.getRelatedPost().getId();
            this.commentId=notification.getRelatedComment().getId();
        }


        if (notification.getType().equals(NotificationType.COMMENT_LIKE)){
            this.postId=notification.getRelatedPost().getId();
            this.commentId=notification.getRelatedComment().getId();
        }

        if (notification.getType().equals(NotificationType.REPLY)){
            this.postId=notification.getRelatedPost().getId();
            this.commentId=notification.getRelatedComment().getId();
            this.replyId=notification.getReply().getId();
        }

        if (notification.getType().equals(NotificationType.REPLAY_LIKE)){
            this.postId=notification.getRelatedPost().getId();
            this.commentId=notification.getRelatedComment().getId();
            this.replyId=notification.getReply().getId();
        }
    }





}
