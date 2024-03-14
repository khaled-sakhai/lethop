package com.example.springsocial.entity.Features;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Notification extends BaseEntity<Long> {
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;
    private boolean isRead=false;
    private boolean isDelivered=false;

    // Association with the related resource
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_comment_id")
    private Comment relatedComment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_post_id")
    private Post relatedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_reply_id")
    private Reply reply;

}
