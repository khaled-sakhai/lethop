package com.example.springsocial.tasks;

import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.entity.Features.UserVerificationCode;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.enums.VerificationType;
import com.example.springsocial.repository.NotificationRepo;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.repository.UserVerificationCodeRepo;
import com.example.springsocial.security.Token.Token;
import com.example.springsocial.security.Token.TokenRepo;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.emailService.EmailSenderService;
import com.example.springsocial.service.postService.PostService;
import com.example.springsocial.service.postService.PostService2;
import com.example.springsocial.service.postService.TagService;
import com.example.springsocial.specification.AppSpecefication;
import com.example.springsocial.util.EmailTemplates;

import com.example.springsocial.util.ProjectUtil;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class WeeklyWork {

    private final UserService userService;
    private final PostService postService;
    private final PostService2 postService2;
    private final TagService tagService;
    private final UserRepo userRepo;
    private final NotificationRepo notificationRepo;
    private final UserVerificationCodeRepo userVerificationCodeRepo;
    private final EmailSenderService emailSenderService;
    private final TokenRepo tokenRepo;
    private final PostRepo postRepo;



    @Scheduled(cron = "0 0 0 * * MON")
    public void sendWeeklyPosts(){
        List<User> users = userRepo.findByIsActiveTrue();
        Sort sort = Sort.by(Sort.Direction.fromString("desc"), "likesCount");
        Pageable paging = PageRequest.of(1, 10, sort);
        List<Post> postsPage= postRepo.findAll(paging).stream().toList();

        for(User user:users){
            // this sends best posts by likes (any tag,any category)// not the best approach
            String userName=user.getUserProfile().getFullName();
            String email = user.getEmail();
            emailSenderService.sendEmail(EmailTemplates.weeklyPostsEmail(userName,email,postsPage));
            //in the future:
            // User 1 preference.
            //List<Tag> userTags=user.getUserInterests();
            // tags posts;
            // make a list of posts (5 from each tag)
            // send an email containing those posts
            //end of work for user 1
        }
    }


    @Scheduled(cron = "0 0 0 */10 * *")
    public void removeOldNotification(){
        /// older than 7 days  if they are read and delivred
        Specification<Notification> spec1 = AppSpecefication.notificationsOlderThan(7);
        spec1.and(AppSpecefication.notificationsRead(true));
        //older than 20 days despite their status.
        Specification<Notification> spec2 = AppSpecefication.notificationsOlderThan(15);
        //combined spec
        Specification<Notification> spec = spec1.or(spec2);

        List<Notification> notfs= notificationRepo.findAll(spec);
        notificationRepo.deleteAllNotifications(notfs);
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void removeOldTokens(){
        List<Token> tokens = tokenRepo.findAllByIsLoggedOut(true);
        tokenRepo.deleteAll(tokens);
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void remindNonVerifiedUser(){
        List<User> users = userRepo.findByIsActiveFalseAndProvider(AuthProvider.local);
        for(User user:users){
            Optional<UserVerificationCode> userVCode= userVerificationCodeRepo.findByUserAndType(user,VerificationType.SIGNUP);
            if(userVCode.isPresent()){
                emailSenderService.sendEmail(EmailTemplates.accountActivationReminderEmail(user.getEmail(),user.getUserProfile().getFullName(),userVCode.get().getConfirmationCode()));
            }
        }
    }

}
