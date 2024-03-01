package com.example.springsocial.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @Size(min = 0, max = 100, message = "Title must be between 0 and 150 characters")
    private String title;
    @NotEmpty(message = "Title must not be empty")
    @Size(min = 100, max = 800, message = "Title must be between 100 and 4000 characters")
    private String content;
    private String tags;
    private String Category;
    
    private boolean isAnonymous=false;
    @JsonProperty("is_tag_modified")
    private boolean isTagModifies=false;
    @JsonProperty("is_category_modified")
    private boolean isCategoryModifies=false;
    @JsonProperty("is_images_modified")
    private boolean isImagesModifies=false;


}
