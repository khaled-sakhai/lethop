package com.example.springsocial.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private String tags;
    private String Category;
    
    private boolean isPublic=true;
    private boolean isAnonymous=false;
    @JsonProperty("is_tag_modified")
    private boolean isTagModifies=false;
    @JsonProperty("is_category_modified")
    private boolean isCategoryModifies=false;
}
