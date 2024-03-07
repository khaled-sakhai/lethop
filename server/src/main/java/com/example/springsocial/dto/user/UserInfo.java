package com.example.springsocial.dto.user;

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
