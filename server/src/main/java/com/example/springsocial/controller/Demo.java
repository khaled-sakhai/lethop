package com.example.springsocial.controller;

import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.security.Token.TokenRepo;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.CommentReplayService;
import com.example.springsocial.service.postService.PostService;

import com.example.springsocial.validator.rateLimiter.WithRateLimitProtection;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@WithRateLimitProtection
@AllArgsConstructor

public class Demo {
    
@Autowired
private PostService postService;
    /// just to test methods
  @Autowired
    private UserService userService;
    @Autowired
    private CommentReplayService commentReplayService;

    @Autowired
    private TokenRepo tokenRepo;
    
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


    @GetMapping("/api/v1/public")
    public String getTokens(){
        //return tokenRepo.findAll();
        return "Hello world";
    }

}
