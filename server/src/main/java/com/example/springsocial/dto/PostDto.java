package com.example.springsocial.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
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
