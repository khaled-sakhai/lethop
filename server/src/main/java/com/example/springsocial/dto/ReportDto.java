package com.example.springsocial.dto;
import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.Features.Report;
import com.example.springsocial.enums.ResourceType;
import com.example.springsocial.util.ProjectUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ReportDto implements Serializable {

    private String feedback;

    private String type;

    @JsonProperty("resource_type")
    private String resourceType;

    private UserInfo reporter;

    private UserInfo reported;

    private String date;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("reply_id")
    private Long replyId;

    private boolean isDeleted=false;

    public ReportDto(Report report){

        this.reported= new UserInfo(report.getRelatedUser());
        this.reporter= new UserInfo(report.getReporter());
        this.isDeleted=report.isDeleted();
        this.resourceType=report.getResourceType().name();
        this.feedback=report.getFeedback();
        this.date= ProjectUtil.convertDateToString(report.getCreatedDate());
        this.type=report.getType().name();

        switch (report.getResourceType()) {
            case USER -> {
            } // No additional data needed for USER reports

            case POST -> this.postId = report.getRelatedPost().getId();
            case COMMENT -> {
                this.postId = report.getRelatedPost().getId();
                this.commentId = report.getRelatedComment().getId();
            }
            case REPLY -> {
                this.postId = report.getRelatedPost().getId();
                this.commentId = report.getRelatedComment().getId();
                this.replyId = report.getRelatedReply().getId();
            }
        }//end switch



    }






}
