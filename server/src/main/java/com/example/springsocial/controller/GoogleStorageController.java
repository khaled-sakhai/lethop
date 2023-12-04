package com.example.springsocial.controller;


import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.service.GoogleCloudService;


@RestController
public class GoogleStorageController {
    
  @Autowired
  private GoogleCloudService googleCloudService;

  //List all file name
  @GetMapping
  public ResponseEntity<List<String>> listOfFiles() {
    List<String> files = googleCloudService.listOfFiles();

    return ResponseEntity.ok(files);
  }

  //Upload file
  @PostMapping("post")
  public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file)
    throws Exception {
    googleCloudService.uploadFile(file, false);
    return ResponseEntity.ok("File uploaded successfully");
  }

  //Upload file
  @PostMapping("profile")
  public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file)
    throws Exception {
    googleCloudService.uploadFile(file, true);

    return ResponseEntity.ok("File uploaded successfully");
  }

  //Delete file
  @DeleteMapping("delete")
  public ResponseEntity<String> deleteFile(@RequestParam String fileName) {
    googleCloudService.deleteFile(fileName);

    return ResponseEntity.ok(" File deleted successfully");
  }

  //Download file
  @GetMapping("download/{name}")
  public ResponseEntity<byte[]> downloadFile(@PathVariable String url) throws IOException {
    byte[] resource = googleCloudService.downloadFile(url);

    HttpHeaders headers = new HttpHeaders();

    headers.add(
      HttpHeaders.CONTENT_DISPOSITION,
      "attachment; filename=\"" + url + "\""
    );
    headers.add("Content-type", MediaType.IMAGE_JPEG_VALUE);

    return ResponseEntity
      .ok()
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .headers(headers)
      .body(resource);
  }

@GetMapping("all")
public List<String> load(){
  return googleCloudService.listOfFiles();
}

}
