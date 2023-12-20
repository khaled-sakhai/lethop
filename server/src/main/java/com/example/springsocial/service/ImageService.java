package com.example.springsocial.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.Image;
import com.example.springsocial.repository.ImageRepo;

@Service
public class ImageService {

    @Autowired
    private ImageRepo imageRepo;
    
    public Image save(Image image){
        return imageRepo.save(image);
    }

    public Image findByFileName(String fileName){
        return imageRepo.findByFileName(fileName);
    }

    public void deletImageByFileName(String fileName){
      // imageRepo.deletByFileName(fileName);
    }

    public Image findImageById(Long imageId) {
        Optional<Image> image = imageRepo.findById(imageId);
        try {
            image.isPresent();
              return image.get();
        } catch (Exception e) {
            
            System.out.println("image doesnt exist");
             return null;
        }
       
    }

    public void deletImage(Image image){
         imageRepo.delete(image);
    }


}
