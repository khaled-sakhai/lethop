package com.example.springsocial.controller;

import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.repository.NotificationRepo;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.repository.UserVerificationCodeRepo;
import com.example.springsocial.security.Token.TokenRepo;
import com.example.springsocial.service.FireBaseService;
import com.example.springsocial.service.NotificationService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.emailService.EmailSenderService;
import com.example.springsocial.service.postService.CommentReplayService;
import com.example.springsocial.service.postService.PostService;

import com.example.springsocial.specification.AppSpecefication;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor

public class Demo {
    
@Autowired
private PostService postService;
    /// just to test methods
  @Autowired
    private UserService userService;
    @Autowired
    private CommentReplayService commentReplayService;
    private final NotificationService notificationService;


    private final FireBaseService imageService;
    private final NotificationRepo notificationRepo;



    @PostMapping("api/v1/public/2")
    public String upload(@RequestParam("file") MultipartFile multipartFile) {
        return imageService.upload(multipartFile,true);
    }

    @PostMapping("api/v1/public/3")
    public void remove(@RequestParam("file") String name) throws IOException {
        imageService.deleteFile(name);
    }

    @GetMapping("/xx")
    public String getsmth(){
        System.out.println("hoklla");
        return "Springoo - this endpoint is not authenticated";
    }

     @GetMapping("/aa")
    public String getsmth1(){
        System.out.println("hoklla");
        return "Springoo authenticated endpoint";
    }

    @GetMapping("api/v1/public/posty")
    public List<Post> getposty(){
        return postService.getbyLikes();
    }


    @GetMapping("/api/v1/public/comment/{postid}")
    public  Set<Comment>  findCommentsByPostId(@PathVariable Long postid) {
        return commentReplayService.getPostComments(postid);
    }

    @GetMapping("/api/v1/public/comment/id/{commentid}")
    public  Comment  findCommentById(@PathVariable Long commentid) {
        return commentReplayService.getCommentById(commentid);
    }

    @GetMapping("/api/v1/public/1")
    public void getToken(){


    }




 

}
