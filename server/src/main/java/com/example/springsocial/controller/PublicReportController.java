package com.example.springsocial.controller;

import com.example.springsocial.dto.ReportRequest;
import com.example.springsocial.entity.Features.Report;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Reply;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.ReportType;
import com.example.springsocial.enums.ResourceType;
import com.example.springsocial.service.ReportService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.CommentReplayService;
import com.example.springsocial.service.postService.PostService2;
import com.example.springsocial.util.PathConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class PublicReportController {


    private final UserService userService;
    private final PostService2 postService2;
    private final ReportService reportService;
    private final CommentReplayService commentReplayService;


    @PostMapping(path = PathConstants.API_V1+"report")
    public ResponseEntity<String> addReport(@RequestBody ReportRequest reportRequest, Principal principal){
        Report report=new Report();
        User reporter = userService.findByEmail(principal.getName()).orElseThrow();
        User reported= null;
        Post post = null;
        Comment comment=null;
        Reply reply=null;
        switch (reportRequest.getResourceType()) {
            case "USER" -> {
                reported=userService.findById(reportRequest.getReported()).orElseThrow();
            }
            case "POST" -> {
                post = postService2.findPostById(reportRequest.getPostId()).orElseThrow();
                reported=post.getUser();
            }
            case "COMMENT" -> {
                post = postService2.findPostById(reportRequest.getPostId()).orElseThrow();
                comment = commentReplayService.findCommentById(reportRequest.getCommentId()).orElseThrow();
                reported=comment.getUser();
            }
            case "REPLY" -> {
                post = postService2.findPostById(reportRequest.getPostId()).orElseThrow();
                comment = commentReplayService.findCommentById(reportRequest.getCommentId()).orElseThrow();
                reply = commentReplayService.findReplyById(reportRequest.getReplyId()).orElseThrow();
                reported=reply.getUser();
            }
        }//end switch

        report.setFeedback(reportRequest.getFeedback());
        report.setReporter(reporter);
        report.setRelatedUser(reported);
        report.setType(ReportType.valueOf(reportRequest.getType()));
        report.setResourceType(ResourceType.valueOf( reportRequest.getResourceType()));
        report.setRelatedPost(post);
        report.setRelatedComment(comment);
        report.setRelatedReply(reply);
        reportService.createReport(report);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Thank you for sending this report!");
    }

}
