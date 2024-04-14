package com.example.springsocial.controller;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.springsocial.dto.comments.CommentRequest;
import com.example.springsocial.dto.comments.CommentResponse;
import com.example.springsocial.dto.comments.ReplyResponse;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.enums.NotificationType;
import com.example.springsocial.service.NotificationService;
import com.example.springsocial.util.PathConstants;
import com.example.springsocial.validator.validators.ValidCommentReplySortBy;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor

@RestController
public class CommentReplayController {
    
    @Autowired
    private CommentReplayService commentReplayService;
    @Autowired
    private UserService userService;

    @Autowired
    private PostService2 postService;
    private final NotificationService notificationService;

    @PostMapping(path = PathConstants.API_V1+"post/{postId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long postId,
                                             @RequestBody @Valid CommentRequest commentRequest,
                                             Principal principal){
        try {
            Comment comment = new Comment();
            comment.setContent(commentRequest.getContent());
            User user = userService.findByEmail(principal.getName()).orElseThrow();
            commentReplayService.addCommentForPost(postId, comment, principal.getName());
            /// notify only if user is not the owner
            if(!Objects.equals(comment.getPost().getUser().getEmail(), principal.getName())){
                notificationService.createNotification(comment.getPost(),comment,user,null, NotificationType.COMMENT);
            }

            return ResponseEntity.ok().body("Comment added successfully");
        } catch (Exception e) {
            // Handle the exception and return ResponseEntity with bad request status
            return ResponseEntity.badRequest().body("Failed to add comment: " + e.getMessage());
        }
    }

    @GetMapping(path = PathConstants.API_V1+PathConstants.API_PUBLIC+"post/{postId}/comments")
    public ResponseEntity<Page<CommentResponse>> getPostComments(@PathVariable Long postId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "lastModifiedDate") @ValidCommentReplySortBy String sortBy,
                                                                 @RequestParam(defaultValue = "20") int size,
                                                                 @RequestParam(defaultValue = "desc") String sortDirection){
        try {

            Page<Comment> commentPage=commentReplayService.findCommentsByPostId(postId,page,size,sortBy,sortDirection);
            if (commentPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Page<CommentResponse> commentsPage = commentPage.map(CommentResponse::new);

            return new ResponseEntity<>(commentsPage, HttpStatus.OK);

        } catch (Exception e) {
            // Handle the exception and return ResponseEntity with bad request status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = PathConstants.API_V1+"/user/comments")
    public ResponseEntity<Page<CommentResponse>> getUserComments(Principal principal,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "lastModifiedDate") @ValidCommentReplySortBy String sortBy,
                                                                 @RequestParam(defaultValue = "20") int size,
                                                                 @RequestParam(defaultValue = "desc") String sortDirection){
        try {
            Page<Comment> commentPage=commentReplayService.findCommentsByUserId(principal.getName(),page,size,sortBy,sortDirection);
            if (commentPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Page<CommentResponse> commentsPage = commentPage.map(CommentResponse::new);

            return new ResponseEntity<>(commentsPage, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path = PathConstants.API_V1+"comment/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @RequestBody @Valid CommentRequest commentRequest,
                                                Principal principal){
        try {
            Comment comment = commentReplayService.findCommentById(commentId).orElseThrow();
            comment.setContent(commentRequest.getContent());
            commentReplayService.editComment(comment);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Comment has been updated successfully");
        }
        catch (Exception e) {
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid request, please try again");
        }
    }
    @DeleteMapping(path = PathConstants.API_V1+"comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                Principal principal){
        try {
            Comment comment = commentReplayService.findCommentById(commentId).orElseThrow();
            commentReplayService.removeComment(comment);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Comment has been removed successfully");
        }
        catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid request, please try again");
        }
    }


    @PostMapping(path = PathConstants.API_V1+"post/{postId}/comment/{commentId}/reply")
    public ResponseEntity<String> addReply(@PathVariable Long postId,
                                             @PathVariable Long commentId,
                                             @RequestBody @Valid CommentRequest commentRequest,
                                             Principal principal){
        try {
            Reply reply = new Reply();
            reply.setContent(commentRequest.getContent());
            commentReplayService.addRelpy(principal.getName(),postId, commentId,reply);
            return ResponseEntity.ok().body("reply has been added successfully");
        } catch (Exception e) {
            // Handle the exception and return ResponseEntity with bad request status
            return ResponseEntity.badRequest().body("Failed to add reply: " + e.getMessage());
        }
    }

    @GetMapping(path = PathConstants.API_V1+PathConstants.API_PUBLIC+"post/{postId}/comment/{commentId}/replies")
    public ResponseEntity<Page<ReplyResponse>> getCommentReplies(@PathVariable Long postId,
                                                                 @PathVariable Long commentId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "lastModifiedDate") @ValidCommentReplySortBy String sortBy,
                                                                 @RequestParam(defaultValue = "20") int size,
                                                                 @RequestParam(defaultValue = "desc") String sortDirection){
        try {
            Page<Reply> replyPage=commentReplayService.findRepliesByComment(commentId,
                    page,size,sortBy,sortDirection);

            if (replyPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Page<ReplyResponse> repliesPage = replyPage.map(ReplyResponse::new);
            return new ResponseEntity<>(repliesPage, HttpStatus.OK);

        } catch (Exception e) {
            // Handle the exception and return ResponseEntity with bad request status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping(path = PathConstants.API_V1+"comment/{replyId}")
    public ResponseEntity<String> updateReply( @PathVariable Long replyId,
                                                @RequestBody @Valid CommentRequest commentRequest,
                                                Principal principal){
        try {
            Reply reply = commentReplayService.findReplyById(replyId).orElseThrow();
            reply.setContent(commentRequest.getContent());
            commentReplayService.editReplay(reply);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("reply has been updated successfully");
        }
        catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid request, please try again");
        }
    }
    @DeleteMapping(path = PathConstants.API_V1+"replay/{replyId}")
    public ResponseEntity<String> deleteReply(
                                                @PathVariable Long replyId,
                                                Principal principal){
        try {
            Reply reply = commentReplayService.findReplyById(replyId).orElseThrow();
            commentReplayService.removeReplay(reply);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("reply has been removed successfully");
        }
        catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid request, please try again");
        }
    }
    
}
