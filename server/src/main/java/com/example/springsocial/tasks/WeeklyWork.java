package com.example.springsocial.tasks;

import com.example.springsocial.entity.Notification.Notification;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.NotificationRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.PostService;
import com.example.springsocial.service.postService.PostService2;
import com.example.springsocial.service.postService.TagService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class WeeklyWork {

    private final UserService userService;
    private final PostService postService;
    private final PostService2 postService2;
    private final TagService tagService;
    private final UserRepo userRepo;
    private final NotificationRepo notificationRepo;


    @Scheduled(cron = "0 0 0 * * MON")
    public void sendWeeklyPosts(){
        List<User> users = userRepo.findAll();
        for(User user:users){
            // User 1 preference.
            List<Tag> userTags=user.getUserInterests();
            // tags posts;
            // make a list of posts (5 from each tag)
            // send an email containing those posts
            //end of work for user 1
        }
    }


    @Scheduled(cron = "0 0 0 */10 * *")
    public void removeOldNotification(){
        List<Notification> notfs= notificationRepo.findAll();
        // find only when notification is 10 days old.
        // remove the notification.
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void removeOldTokens(){
        List<User> users = userRepo.findAll();
        for(User user:users){

        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void remindNonVerifiedUser(){
        List<User> users = userRepo.findAll();
        for(User user:users){
        }
    }

}
