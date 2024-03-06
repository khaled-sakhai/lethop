package com.example.springsocial.dto.comments;

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
public class CommentRequest {

    @NotEmpty(message = "Comment must not be empty")
    @Size(min = 3, max = 400, message = "Comment must be between 3 and 400 characters")
    @JsonProperty("content")
    private String content;
    
  
}
