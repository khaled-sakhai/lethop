package com.example.springsocial.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springsocial.entity.postRelated.Post;
import com.google.cloud.storage.Blob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.Image;
import com.example.springsocial.repository.ImageRepo;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private GoogleCloudService googleCloudService;
    
    public Image save(Image image){
        return imageRepo.save(image);
    }

    public List<Image> saveListImagePost(List<MultipartFile> postImages) throws Exception {
        List<Image> postImagesList=new ArrayList<>();
        for(MultipartFile imgFile:postImages){
            Blob imgBlob=googleCloudService.uploadFile(imgFile, false);
            Image image = new Image(imgBlob.getMediaLink(),imgBlob.getName());
            postImagesList.add(image);
            this.save(image);
        }
        return postImagesList;
    }
    public void removeImagesFromPost(Post post){
        imageRepo.deleteAll(post.getPostImages());
        post.getPostImages().clear();
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
