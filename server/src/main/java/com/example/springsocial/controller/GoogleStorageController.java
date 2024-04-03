//package com.example.springsocial.controller;
//
//
//import java.util.List;
//
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.example.springsocial.service.GoogleCloudService;
//import com.google.cloud.storage.Blob;
//
////This class is just for testing google storage ---
//@RestController
//@AllArgsConstructor
//
//public class GoogleStorageController {
//
//  @Autowired
//  private GoogleCloudService googleCloudService;
//
//
//
//  //List all file name + fileUrl --- this can be customized in the googleCloudService class
//  @GetMapping("/allfiles")
//  public ResponseEntity<List<String>> listOfFiles() {
//    List<String> files = googleCloudService.listOfFiles();
//
//    return ResponseEntity.ok(files);
//  }
//
//  //Upload file
//  @PostMapping("post")
//  public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file)
//    throws Exception {
//   Blob blob =  googleCloudService.uploadFile(file, false);
//   String imageUrl = blob.getMediaLink();
//   String imageFileName = blob.getName();
//  //  Image image = new Image(imageUrl,imageFileName);
//  //  imageRepo.save(image);
//
//    return ResponseEntity.ok("File uploaded successfully" +"name: "+imageFileName +"url:" + imageUrl);
//  }
//
//
//  //Delete file
//  @DeleteMapping("delete")
//  public ResponseEntity<String> deleteFile(@RequestParam String fileName) {
//    googleCloudService.deleteFile(fileName);
//
//    return ResponseEntity.ok(" File deleted successfully");
//  }
//
//  //Download file
//  // @GetMapping("download/{name}")
//  // public ResponseEntity<byte[]> downloadFile(@PathVariable String url) throws IOException {
//  //   byte[] resource = googleCloudService.downloadFile(url);
//
//  //   HttpHeaders headers = new HttpHeaders();
//
//  //   headers.add(
//  //     HttpHeaders.CONTENT_DISPOSITION,
//  //     "attachment; filename=\"" + url + "\""
//  //   );
//  //   headers.add("Content-type", MediaType.IMAGE_JPEG_VALUE);
//
//  //   return ResponseEntity
//  //     .ok()
//  //     .contentType(MediaType.APPLICATION_OCTET_STREAM)
//  //     .headers(headers)
//  //     .body(resource);
//  // }
//
//@GetMapping("all")
//public List<String> load(){
//  return googleCloudService.listOfFiles();
//}
//
//}
