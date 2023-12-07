package com.example.springsocial.service;

import java.io.IOException;
import java.util.ArrayList;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import com.example.springsocial.repository.ImageRepo;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Component
public class GoogleCloudService {
    
  @Value("${gcp.bucket.name}")
  private String bucketName;

    @Autowired
    private Storage storage;

  @Autowired
  private ImageRepo imageRepo;


  public List<String> listOfFiles() {
    List<String> list = new ArrayList<>();
    Page<Blob> blobs = storage.list(bucketName);
    for (Blob blob : blobs.iterateAll()) {
      list.add("File_Name:"+blob.getName()+" , FileUrl: "+ blob.getMediaLink());
    }
    return list;
  }
 

  // delete is only possible by fileName
  public boolean deleteFile(String fileName) {
    Blob blob = storage.get(bucketName, fileName);
    return blob.delete();
  }


  /// this method returns the Blob of the image being uploaded /// uploaded to my google cloud storage 
  // to access content: 
  //  blob.getName() --- returns the name of the file ie: image2.jpg
  //  blob.getMediaLink() returns the full url (serving url) ie: google.com/asdasd/545/image2.jpg

  public Blob uploadFile(MultipartFile file, boolean isProfile)
    throws Exception {
    //check if its an image or not
    if (!isFileAnImage(file.getOriginalFilename())) {
      throw new Exception(
        "Image type is invalid, please use:jpg, jpeg, png, gif"
      );
    }
    ///generate id for file names;
    // if profile pic the url will be : google.../profile/xxx.jpg if post: google.../post/xxx.jpg
   String type = isProfile ? "profile" : "post";
   String fileName = generateFileName(file.getOriginalFilename(), type);

    ///google cloud storage upload
    BlobId blobId = BlobId.of(bucketName,fileName);
    BlobInfo blobInfo = BlobInfo
      .newBuilder(blobId)
      .setContentType(file.getContentType())
      .build();
    Blob blob = storage.create(blobInfo, file.getBytes());
    return blob;
  }


  //helper methods
  private boolean isFileAnImage(String fileName) {
    String[] fileNameParts = fileName.split("\\.");
    String fileExtenstion = fileNameParts[fileNameParts.length - 1];
    String[] allowedExt = { "jpg", "jpeg", "png", "gif" };
    for (String extenstion : allowedExt) {
      if (fileExtenstion.equalsIgnoreCase(extenstion)) {
        return true;
      }
    }
    return false;
  }

  private String generateFileName(String fileName, String type) {
    return type + "/image-" + new Date().getTime() + "" + fileName;
  }


   /// those are extra functionalities
  
  // public  byte[] downloadFile(String imgUrl) throws IOException {
  //   Blob blob = storage.get(bucketName, imgUrl);
  //   ByteArrayResource resource = new ByteArrayResource(blob.getContent());
  //   return resource.getByteArray();
  // }


  // public  String generateServingUrl(String imgUrl) throws IOException {
  //   Blob blob = storage.get(bucketName, imgUrl);
  //   return blob.getMediaLink();
  // }


}
