package com.example.springsocial.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.CommentReplayService;
import com.example.springsocial.service.postService.PostService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class CommentReplayLikeController {
    
    @Autowired
    private CommentReplayService commentReplayService;
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;


        @PostMapping(path = "api/v1/post/{postid}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postid,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).get();
      Optional<Post> post = postService.findById(postid);
      if(post.isPresent()){

        postService.likePost( user,post.get());

      return ResponseEntity.ok("Post liked succesfully");
      }
      return ResponseEntity.badRequest().body("Post was not liked succesfully");     
    }

    @PostMapping(path = "api/v1/post/{postid}/unlike")
    public ResponseEntity<String> unlikePost(@PathVariable Long postid,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).get();
      Optional<Post> post = postService.findById(postid);
      if(post.isPresent()){

        postService.unlikePost(user, post.get());

      return ResponseEntity.ok("Post unliked succesfully");
      }
      return ResponseEntity.badRequest().body("Post was not unliked succesfully");     
    }


    
}
