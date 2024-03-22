package com.example.springsocial.controller.user;

import com.example.springsocial.dto.NotificationDto;
import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.NotificationService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.PostService2;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor


public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping(path = "api/v1/user/notifications")
    public ResponseEntity<Page<NotificationDto>> getAllNotification(Principal principal,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
                                                                    @RequestParam(defaultValue = "8") int size,
                                                                    @RequestParam(defaultValue = "desc") String sortDirection){
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        Page<NotificationDto> notifs = notificationService.deliverNotificationByUserId(user.getId(),page,size,sortBy,sortDirection);
        if (notifs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(notifs, HttpStatus.OK);
    }

    @PutMapping("api/v1/user/notifications/")
    public ResponseEntity<String> readAll(Principal principal){
        User user = userService.findByEmail(principal.getName()).orElseThrow();
        notificationService.readAllNotifs(user.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("All notification has been marked read!");
    }

    @PutMapping(path = "api/v1/notification/{notificationId}")
    public ResponseEntity<NotificationDto> readNotification(@PathVariable Long notificationId,Principal principal){
        Notification notification = notificationService.findById(notificationId).orElseThrow();
        notificationService.readNotification(notification);
        return ResponseEntity.ok().body(new NotificationDto(notification));
    }

}
