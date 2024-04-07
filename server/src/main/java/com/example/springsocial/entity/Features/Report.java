package com.example.springsocial.entity.Features;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.ReportType;
import com.example.springsocial.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "Reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Reports")
@SQLDelete(sql = "UPDATE Reports SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Report extends BaseEntity<Long> {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_post_id")
    private Post relatedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    private User relatedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_comment_id")
    private Comment relatedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_reply_id")
    private Reply relatedReply;

    private String feedback;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Enumerated(EnumType.STRING)
    private ReportType type;


}
