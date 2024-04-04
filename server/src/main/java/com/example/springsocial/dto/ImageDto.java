package com.example.springsocial.dto;
import com.example.springsocial.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ImageDto {
    private String url;
    private String fileName;

    public ImageDto(Image image){
        this.url=image.getUrl();
        this.fileName=image.getFileName();
    }
}
