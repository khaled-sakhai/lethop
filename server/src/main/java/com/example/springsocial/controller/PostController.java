package com.example.springsocial.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.dto.PostDto;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.service.CategoryService;
import com.example.springsocial.service.GoogleCloudService;
import com.example.springsocial.service.PostService;
import com.example.springsocial.service.TagService;
import com.example.springsocial.service.UserService;
import com.google.cloud.storage.Blob;


@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserService userService;
 @Autowired
  private CategoryService categoryService;

  @Autowired
  private GoogleCloudService googleCloudService;

  @Autowired
  private TagService tagService;

    @PostMapping(path = "api/v1/post/create")
    public ResponseEntity<String> createPost(@RequestPart("post") PostDto posttDto,
    @RequestParam(required = false) MultipartFile postImage,
        Principal principal) throws Exception{
        // convert Dao to Post
        Post newPost = new Post();
        newPost.setTitle(posttDto.getTitle());
        newPost.setContent(posttDto.getContent());
        //tag

        String [] items = posttDto.getTags().split("\\s*,\\s*");
        Set<Tag> tags = new HashSet<>();
        for(String tag: items){
            Tag tagDb = tagService.saveTag(new Tag(tag));
            tags.add(tagDb);
        }
        newPost.setListTags(tags);
        /// post settings
        newPost.setPublic(posttDto.isPublic());
        newPost.setAnonymous(posttDto.isAnonymous());
        ///category
        Category categoryObj = new Category(posttDto.getCategory());
        Category category = categoryService.saveCategory(categoryObj);
        category.getPosts().add(newPost);
        newPost.setCategory(category);    
        ///images
        if(postImage!=null){
        Blob imgBlob=googleCloudService.uploadFile(postImage, false);
        Image image = new Image(imgBlob.getMediaLink(),imgBlob.getName());
        imageRepo.save(image);
        newPost.setPostImage(image);
        }
        /// set user for the post
        User user= userService.findByEmail(principal.getName()).get();
        newPost.setUser(user);
        user.getPosts().add(newPost);
        //save post
       postService.createNewPost(newPost);
       return ResponseEntity.ok("Post added succesfully");
    }

    @PostMapping(path = "api/v1/post/edit/{postid}")
    public ResponseEntity<String> editPost(@PathVariable(required = true) Long postid,@RequestPart("post") PostDto posttDto, 
                                           @RequestParam(required = false) MultipartFile postImage,
                                           Principal principal){
        Optional<Post> postObj = postService.findById(postid);
        if(postObj.isPresent()){
            Post post = postObj.get();
            User user= userService.findByEmail(principal.getName()).get();
            if(postService.isPostUserMatch(user, post)){
                   ///post settings
                   post.setPublic(posttDto.isPublic());
                   post.setAnonymous(posttDto.isAnonymous());
                   //title
                   post.setTitle(posttDto.getTitle());
                   //// content
                   post.setContent(posttDto.getContent());
                   //tags
                   if(posttDto.isTagModifies()){
                     Set<Tag> oldTags= post.getListTags();
                     String [] itemsTags = posttDto.getTags().split("\\s*,\\s*");
                     Set<Tag> tags = new HashSet<>();
                     for(String tag: itemsTags){
                        Tag tagDb = tagService.saveTag(new Tag(tag));
                        tags.add(tagDb);
                      }

                    post.setListTags(tags);
                   }
                   // update category
                   if(posttDto.isCategoryModifies() &&
                   !posttDto.getCategory().toLowerCase().equalsIgnoreCase(post.getCategory().getCategory())){

                    post.getCategory().removePostFromCategoryById(postid);

                    Category categoryObj = new Category(posttDto.getCategory());
                    Category category = categoryService.saveCategory(categoryObj);
                    category.getPosts().add(post);
                    post.setCategory(category);
                   }
                   ///update post
                   postService.updatePost(post);
                   return ResponseEntity.ok("Post updated succesfully");
            }
        }

        
         return ResponseEntity.badRequest().body("Post was not updated succesfully");     
    }


    @DeleteMapping(path = "api/v1/post/{postid}/delete")
    public ResponseEntity<String> removePost(@PathVariable Long postid,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).get();
      Optional<Post> post = postService.findById(postid);
      if(post.isPresent() && post.get().getUser()==user){
        Post postObj = post.get();
        postObj.getCategory().removePostFromCategoryById(postObj.getId());
        postService.removePost(postObj, user);
        return ResponseEntity.ok("Post removed succesfully");
      }
    
      return ResponseEntity.badRequest().body("Post was not removed succesfully");     
    }


    @PostMapping(path = "api/v1/post/{postid}/save")
    public ResponseEntity<String> savePost(@PathVariable Long postid,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).get();
      Optional<Post> post = postService.findById(postid);
      if(post.isPresent()){

        postService.savePost(post.get(), user);

      return ResponseEntity.ok("Post saved succesfully");
      }
      return ResponseEntity.badRequest().body("Post was not saved succesfully");     
    }

    @PostMapping(path = "api/v1/post/{postid}/unsave")
    public ResponseEntity<String> unSavePost(@PathVariable Long postid,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).get();
      Optional<Post> post = postService.findById(postid);
      if(post.isPresent()){

        postService.removePostFromUserSavedList(user, post.get());

      return ResponseEntity.ok("Post unsaved succesfully");
      }
      return ResponseEntity.badRequest().body("Post was not unsaved succesfully");     
    }

    @GetMapping("api/v1/public/posts/tag/{tag}")
    public ResponseEntity<List<Post>> getPostsByTag(@PathVariable String tag){
      List<Post> posts= postService.findByTag(tag);
      return new ResponseEntity<>(posts, HttpStatus.OK);
    }

}
