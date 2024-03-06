package com.example.springsocial.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {

    private Long userId;
    private String userName;
    private String userImageUrl;
}
