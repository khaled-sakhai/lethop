package com.example.springsocial.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserInfo {
    private Long userId;
    private String userName;
    private String userImageUrl;

}
