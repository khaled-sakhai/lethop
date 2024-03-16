package com.example.springsocial.controller.user;

import com.example.springsocial.dto.NotificationDto;
import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.NotificationService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.PostService2;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor


public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final PostService2 postService2;

    @GetMapping(path = "api/v1/user/notifications")
    public ResponseEntity<List<NotificationDto>> getAllNotification(Principal principal){
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        List<Notification> notifs = notificationService.findAllNotificationByUserId(user.getId());

        List<NotificationDto> notifsDtos = notifs.stream().map(NotificationDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(notifsDtos, HttpStatus.OK);
    }

    @PutMapping(path = "api/v1/notification/{notificationId}")
    public ResponseEntity<String> readNotification(@PathVariable Long notificationId,Principal principal){
        Notification notification = notificationService.findById(notificationId).orElseThrow();
        notificationService.readNotification(notification);
        return ResponseEntity.ok().body("Notification has been marked - read!");
    }

}
