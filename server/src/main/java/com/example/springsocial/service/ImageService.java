package com.example.springsocial.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.springsocial.entity.Image;
import com.example.springsocial.repository.ImageRepo;

public class ImageService {

    @Autowired
    private ImageRepo imageRepo;
    
    public Image addImageUrlToDB(Image image){
        
        return imageRepo.save(image);
    }


}
