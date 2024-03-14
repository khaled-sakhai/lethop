package com.example.springsocial.dto;

import com.example.springsocial.dto.user.UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ReportRequest {
    private String feedback;

    private String type;

    @JsonProperty("resource_type")
    private String resourceType;

    private Long reported;

    private String date;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("reply_id")
    private Long replyId;
}
