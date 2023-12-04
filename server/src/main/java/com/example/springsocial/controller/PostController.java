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
//       @PreAuthorize("hasAuthority('ROLE_USER')")
//     public ResponseEntity<String> createPost(@RequestPart("post") PostDto posttDto,
//       @RequestParam(required = false) List<MultipartFile> images,
       
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


//     @PostMapping(path = "api/v1/post/update/image")
//     @PreAuthorize("hasAuthority('ROLE_USER')")
//     public ResponseEntity<String> createPost(
//         @RequestParam(required = false) List<MultipartFile> images,
//         Long postId
//         ) throws Exception{
//           Post post = postService.findById(postId).orElse(null);
//           if(post!=null){
//             post=postService.uploadImagesForNewPost(post, images);
//             postService.updatePost(post);
//           }
         
//         return ResponseEntity.ok("Images added to post");
//     }


//     @GetMapping(path = "api/v1/post/get/{postId}")
//     public PostDao getPost(@PathVariable Long postId) throws IOException{
//       Post post = postService.findById(postId).orElseThrow(null);
//       PostDao postDao = new PostDao();
//       postDao.setContent(post.getContent());
//       postDao.setTitle(post.getTitle());
//       postDao.setUserName(post.getUser().getUserProfile().getFirstName() + " " + post.getUser().getUserProfile().getLastName());
//       postDao.setCreatedAt(post.getCreatedDate().toString());
//       if(post.getListImages().size()>0){
//         for(Image imgUrl:post.getListImages()){
//          postDao.getPostListImages().add( googleCloudService.generateServingUrl(imgUrl.getUrl()));
//         }
//       }
//       return postDao;
//     }

//     @GetMapping(path = "api/v1/post/get/all/{userId}")
//     public List<PostDao> getPostsByUserId(@PathVariable Long userId){
//       List<PostDao> posts = new ArrayList<>();
//       for(Post post:postService.findByUserId(userId)){
//         PostDao postDao = new PostDao();
//         postDao.setContent(post.getContent());
//         postDao.setCreatedAt(post.getCreatedDate().toString());
//         postDao.setTitle(post.getTitle());
        
//         postDao.setUserName(post.getUser().getEmail());
//         post.getListImages().forEach(e-> {
//           try {
//             postDao.getPostListImages().add(googleCloudService.generateServingUrl(e.getUrl()));
//           } catch (IOException e1) {
//             e1.printStackTrace();
//           }
//         });
//         posts.add(postDao);
//       }
//       return posts;
//     }

// /*
// user register (email + password)
// email sent with code reg or link
// user activated
// user login + fill profile form
//  */


// }
