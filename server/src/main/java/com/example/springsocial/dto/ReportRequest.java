package com.example.springsocial.dto;

import com.example.springsocial.dto.user.UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ReportRequest {

    @NotEmpty(message = "Email must not be empty")
    @Size(min = 4, max = 500, message = "Email must be between 4 and 500 characters")
    private String feedback;

    @NotEmpty(message = "Type must not be empty")
    @Size(min = 4, max = 10, message = "Type must be between 4 and 10 characters")
    private String type;

    @JsonProperty("resource_type")
    @NotEmpty(message = "ResourceType must not be empty")
    @Size(min = 4, max = 10, message = "ResourceType must be between 4 and 10 characters")
    private String resourceType;

    @JsonProperty("reported_user")
    private Long reported;

    @NotEmpty(message = "post_id must not be empty")
    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("reply_id")
    private Long replyId;
}
