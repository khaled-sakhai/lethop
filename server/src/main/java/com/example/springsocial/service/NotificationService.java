package com.example.springsocial.service;

import com.example.springsocial.dto.NotificationDto;
import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.NotificationType;
import com.example.springsocial.repository.NotificationRepo;
import com.example.springsocial.service.emailService.EmailSenderService;
import com.example.springsocial.specification.AppSpecefication;
import com.example.springsocial.util.EmailTemplates;
import com.example.springsocial.validator.permessions.NotificationOwner;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class NotificationService {
    private final NotificationRepo notificationRepo;
    private final EmailSenderService emailSenderService;



    public void createNotification(Post post, Comment comment, User user, Reply reply,NotificationType type){
        Notification notification = new Notification();
        notification.setType(type);
        notification.setFromUser(user);
        notification.setRelatedPost(post);

        if (type==NotificationType.POST_LIKE){
            notification.setUser(post.getUser());
        }
        if (type==NotificationType.COMMENT){
            notification.setRelatedComment(comment);
            notification.setUser(post.getUser());
        }
        if (type==NotificationType.COMMENT_LIKE){
            notification.setRelatedComment(comment);
            notification.setUser(comment.getUser());
        }
        if (type==NotificationType.REPLY){
            notification.setReply(reply);
            notification.setRelatedComment(comment);
            notification.setUser(comment.getUser());
        }
        if (type==NotificationType.REPLAY_LIKE){
            notification.setReply(reply);
            notification.setUser(reply.getUser());
            notification.setRelatedComment(comment);
        }

        notificationRepo.save(notification);
        // send email only if user allow email notification
        if(notification.getUser().getUserProfile().isNotificationEmailed()){
            this.sendNotificationEmail(notification);
        }
    }

    public Page<NotificationDto> deliverNotificationByUserId(Long userId,int pageNo,int pageSize,String sortBy,String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        Page<Notification> notificationsPage= notificationRepo.findByUserIdAndIsRead(userId,false,paging);
        //to be returned;
        return notificationsPage.map(NotificationDto::new);
    }

    public void readAllNotifs(Long userId){
        Pageable pageable = Pageable.unpaged();
        Page<Notification> notis=notificationRepo.findByUserIdAndIsRead(userId,false,pageable);
        notis.getContent().forEach(n->n.setRead(true));
        notificationRepo.saveAll(notis.getContent());
    }

    @NotificationOwner
    public Notification readNotification(Notification notification){
        notification.setRead(true);
        return notificationRepo.save(notification);
    }

    public Optional<Notification> findById(Long id){
        return notificationRepo.findById(id);
    }

    public void sendNotificationEmail(Notification notification){
        SimpleMailMessage mailMessage;
        if(notification.getType()==NotificationType.COMMENT ||notification.getType()==NotificationType.POST_LIKE  ) {
            mailMessage = EmailTemplates.postLikedOrCommentedEmail(notification.getUser().getEmail(), notification.getUser().getUserProfile().getFullName(), notification.getRelatedPost().getId() + "", notification.getRelatedPost().getTitle());
            emailSenderService.sendEmail(mailMessage);
        }
    }
}
