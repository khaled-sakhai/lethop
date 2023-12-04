package com.example.springsocial.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDto {
    private String title;
    private String content;
    private String tag;
}
