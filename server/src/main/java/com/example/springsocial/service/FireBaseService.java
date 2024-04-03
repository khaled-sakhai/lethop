package com.example.springsocial.service;

import com.example.springsocial.util.Constants;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.UUID;

@Service
public class FireBaseService {


    public String upload(MultipartFile multipartFile,boolean isProfile) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original
            if (!this.isFileAnImage(fileName)){
                throw  new RuntimeException("File type is not supported! please use one of the following: jpg, jpeg, png, gif");
            }
            String type = isProfile ? "profile" : "post";
           fileName = this.generateFileName(fileName,type);
            this.uploadFile(multipartFile,fileName);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }

    public void deleteFile(String fileName) throws IOException {
        Storage storage = this.getStorage();
        BlobId blobId = BlobId.of(Constants.BUCKET_NAME, fileName);
        try {
            Blob blob = storage.get(blobId);
            blob.delete();

            System.out.println("File deleted successfully.");
        }
        catch (Exception e){
            System.out.println("Failed to delete file: " + fileName);
        }
    }

    //helper
    protected void uploadFile(MultipartFile file, String name) throws IOException {
        Storage storage = this.getStorage();
        BlobId blobId = BlobId.of(Constants.BUCKET_NAME,name);
        BlobInfo blobInfo = BlobInfo
                .newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());
    }
    private Storage getStorage() throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        return StorageClient.getInstance().bucket().getStorage();
    }
    private String generateFileName(String fileName, String type) {
        // returns ==> directory : post or profile / currentdate + fileName --- this ensures all files are unique
        return type + "/image-" + new Date().getTime() + "-" + fileName;
    }
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    public boolean isFileAnImage(String fileName) {
        String fileExtension = this.getExtension(fileName);
        String[] allowedExt = { ".jpg", ".jpeg", ".png", ".gif" };
        for (String extension : allowedExt) {
            if (fileExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

}

