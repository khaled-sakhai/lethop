package com.example.springsocial.dto;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class PostSuccessResponse {
    private String title;
    private String userName;
    private String createdAt;
    private String content;
    private List<String> postListImages = new ArrayList<>();
    

}
