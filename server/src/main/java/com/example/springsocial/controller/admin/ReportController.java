package com.example.springsocial.controller.admin;

import com.example.springsocial.dto.NotificationDto;
import com.example.springsocial.dto.ReportDto;
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
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor

public class ReportController {

    private final UserService userService;
    private final PostService2 postService2;
    private final ReportService reportService;
    private final CommentReplayService commentReplayService;

    @GetMapping(path = "api/admin/reports")
    public ResponseEntity<Page<ReportDto>> getAllReports(Principal principal,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "lastModifiedDate") String sortBy,
                                                         @RequestParam(defaultValue = "8") int size,
                                                         @RequestParam(defaultValue = "desc") String sortDirection){
        Page<ReportDto> reportDtos = reportService.getAllReports(page,size,sortBy,sortDirection);
        if (reportDtos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reportDtos, HttpStatus.OK);
    }

    @PostMapping(path = "api/v1/report")
    public ResponseEntity<String> addReport(@RequestBody ReportRequest reportRequest, Principal principal){
        Report report=new Report();
        User reporter = userService.findByEmail(principal.getName()).orElseThrow();
        User reported= null;
        Post post = null;Comment comment=null;Reply reply=null;
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