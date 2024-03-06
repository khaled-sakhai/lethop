package com.example.springsocial.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.springsocial.dto.comments.CommentRequest;
import com.example.springsocial.dto.comments.CommentResponse;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.CommentReplayService;
import com.example.springsocial.service.postService.PostService2;


import javax.validation.Valid;

@RestController
public class CommentReplayController {
    
    @Autowired
    private CommentReplayService commentReplayService;
    @Autowired
    private UserService userService;

    @Autowired
    private PostService2 postService;

    @PostMapping(path = "api/v1/post/{postId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long postId,
                                             @RequestBody @Valid CommentRequest commentRequest,
                                             Principal principal){
        try {
            Comment comment = new Comment();
            comment.setContent(commentRequest.getContent());
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            System.out.println(commentRequest.getContent());
            System.out.println(comment.getContent());
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

            commentReplayService.addCommentForPost(postId, comment, principal.getName());
            return ResponseEntity.ok().body("Comment added successfully");
        } catch (Exception e) {
            // Handle the exception and return ResponseEntity with bad request status
            return ResponseEntity.badRequest().body("Failed to add comment: " + e.getMessage());
        }
    }

    @GetMapping(path = "api/v1/public/post/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getPostComments(@PathVariable Long postId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                                                 @RequestParam(defaultValue = "20") int size,
                                                                 @RequestParam(defaultValue = "desc") String sortDirection){
        try {

            Page<Comment> commentPage=commentReplayService.findCommentsByPostId(postId,page,size,sortBy,sortDirection);
            if (commentPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // Get the content (comments) from the Page object
            List<CommentResponse> commentsList = commentPage.getContent().stream()
                    .map(CommentResponse::new)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(commentsList, HttpStatus.OK);

        } catch (Exception e) {
            // Handle the exception and return ResponseEntity with bad request status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    
}
