package com.example.springsocial.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.util.ProjectUtil;
import com.google.cloud.storage.Blob;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.Image;
import com.example.springsocial.repository.ImageRepo;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageService {

    @Autowired
    private ImageRepo imageRepo;


    private final FireBaseService fireBaseService;

    public Image save(Image image){
        return imageRepo.save(image);
    }

    public List<Image> saveListImagePost(List<MultipartFile> postImages) throws Exception {
        List<Image> postImagesList=new ArrayList<>();
        for(MultipartFile imgFile:postImages){
            String imgName=fireBaseService.upload(imgFile, false);
            Image image = new Image(ProjectUtil.getMediaUrl(imgName),imgName);
            imageRepo.save(image);
            postImagesList.add(image);
        }
        return postImagesList;
    }
    public void removeImagesFromPost(Post post){
        for (Image img:post.getPostImages()){
            imageRepo.delete(img);
        }
    }

    public void removeImagesFromStorage(Post post) throws IOException {
        List<Image> postImagesList=post.getPostImages();
        for (Image img:postImagesList){
            fireBaseService.deleteFile(img.getFileName());
        }
    }
    public void finalDeleteImages(Image image) throws IOException {
            fireBaseService.deleteFile(image.getFileName());
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
