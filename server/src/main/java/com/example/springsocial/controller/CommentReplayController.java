package com.example.springsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.CommentReplayService;
import com.example.springsocial.service.postService.PostService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class CommentReplayController {
    
    @Autowired
    private CommentReplayService commentReplayService;
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping(path = "api/v1/post/{postid}/comment")
    public ResponseEntity<String> commentOnPost(@PathVariable(required = true) Long postid ){
        return null;
    }
}
