package com.example.springsocial.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentRequest {

    @NotEmpty(message = "Comment must not be empty")
    @Size(min = 3, max = 400, message = "Comment must be between 3 and 400 characters")
    private String content;
    
  
}
