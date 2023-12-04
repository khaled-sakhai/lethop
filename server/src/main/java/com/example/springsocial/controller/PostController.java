// package com.example.springsocial.controller;

// import java.io.IOException;
// import java.security.Principal;
// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestPart;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.springsocial.Dao.PostDao;
// import com.example.springsocial.dto.PostDto;
// import com.example.springsocial.entity.Image;
// import com.example.springsocial.entity.Post;
// import com.example.springsocial.entity.User;
// import com.example.springsocial.repository.ImageRepo;
// import com.example.springsocial.service.GoogleCloudService;
// import com.example.springsocial.service.PostService;
// import com.example.springsocial.service.UserService;


// @RestController
// public class PostController {

//     @Autowired
//     private PostService postService;

//     @Autowired
//     private ImageRepo imageRepo;

//     @Autowired
//     private UserService userService;
//  @Autowired
//   private GoogleCloudService googleCloudService;

//     @PostMapping(path = "api/v1/post/create")
//     @PreAuthorize("hasAuthority('ROLE_USER')")
//     public ResponseEntity<String> createPost(@RequestPart("post") PostDto posttDto,
//     @RequestParam(required = false) List<MultipartFile> images,
//         Principal principal) throws Exception{
//         // convert Dao to Post  
//         Post newPost = new Post();
//         newPost.setTitle(posttDto.getTitle());
//         newPost.setContent(posttDto.getContent());
//         newPost.setTagPost(posttDto.getTag());
//         ///images
//         newPost = postService.uploadImagesForNewPost(newPost, images);
  
//         /// set user for the post
//         User user= userService.findByEmail(principal.getName()).get();
//         newPost.setUser(user);
//         //save post
//        postService.createNewPost(newPost);
//        return ResponseEntity.ok("Post added succesfully");
//     }




 

// }
