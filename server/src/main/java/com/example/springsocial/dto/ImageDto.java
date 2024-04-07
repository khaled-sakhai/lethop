package com.example.springsocial.dto;
import com.example.springsocial.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ImageDto implements Serializable  {
    private Long id;
    private String url;
    private String fileName;

    public ImageDto(Image image){
        if (image!=null){
            this.id=image.getId();
            this.url=image.getUrl();
            this.fileName=image.getFileName();
        }
    }
}
