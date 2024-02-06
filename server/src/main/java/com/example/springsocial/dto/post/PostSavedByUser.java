package com.example.springsocial.dto.post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSavedByUser {
    private String userName;
    private Long userId;
    
}
