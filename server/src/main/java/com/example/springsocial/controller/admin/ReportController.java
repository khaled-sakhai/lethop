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
import com.example.springsocial.util.PathConstants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(path = PathConstants.API_V1+PathConstants.ADMIN_END_POINT)

public class ReportController {

    private final UserService userService;
    private final PostService2 postService2;
    private final ReportService reportService;
    private final CommentReplayService commentReplayService;

    @GetMapping(path = "reports")
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

    @GetMapping(path = "report/{reportId}")
    public ResponseEntity<ReportDto> getRepportById(Principal principal,
                                                    @PathVariable Long reportId){
        Report report= reportService.findById(reportId).orElseThrow();
        return new ResponseEntity<>(new ReportDto(report), HttpStatus.OK);
    }

    @DeleteMapping(path = "report/{reportId}")
    public ResponseEntity<String> removeReportById(Principal principal,
                                                    @PathVariable Long reportId){
       reportService.deleteReport(reportId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Report has been removed");
    }





}
