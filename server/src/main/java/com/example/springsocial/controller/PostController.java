package com.example.springsocial.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.example.springsocial.dto.comments.ReplyResponse;
import com.example.springsocial.dto.user.UserInfo;
import com.example.springsocial.entity.Image;
import com.example.springsocial.enums.NotificationType;
import com.example.springsocial.service.*;
import com.example.springsocial.service.postService.PostService2;
import com.example.springsocial.util.PathConstants;
import com.example.springsocial.util.ProjectUtil;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.dto.post.PostRequest;
import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.service.postService.CategoryService;
import com.example.springsocial.service.postService.PostService;
import com.example.springsocial.service.postService.TagService;
import com.example.springsocial.util.Constants;

@AllArgsConstructor

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostService2 postService2;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserService userService;
 @Autowired
  private CategoryService categoryService;

 private final FireBaseService fireBaseService;

  @Autowired
  private TagService tagService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private UtilService utilService;

  private final NotificationService notificationService;



    @GetMapping(PathConstants.API_V1+PathConstants.API_PUBLIC+"feed1")
    public  ResponseEntity<Map<String, Object>>  getFeedByCursor(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String cursorPostId,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) throws ParseException {
        List<PostDto> postDos = postService2.getFeedByCursor(category, tag, cursorPostId, size, sortBy, sortDirection);
        if (postDos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Generate the next cursor directly

        PostDto lastPost = postDos.get(postDos.size()-1);
        String newCursor = String.format("%s:%s,id:%s", sortBy,
                lastPost.getSortValue(sortBy),
                lastPost.getPostId());

        Map<String, Object> response = new HashMap<>();
        response.put("posts", postDos);
        response.put("cursor", newCursor);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



  @GetMapping(PathConstants.API_V1+PathConstants.API_PUBLIC+"feed")
  public  ResponseEntity<Page<PostDto>> getFeed(
  @RequestParam(required = false) String category,
  @RequestParam(required = false) String tag,
  @RequestParam(defaultValue = "0") int page,
  @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
  @RequestParam(defaultValue = "20") int size,
  @RequestParam(defaultValue = "desc") String sortDirection
  ){
      Page<PostDto> postDtos = postService2.getFeedPosts(category, tag, page, size, sortBy, sortDirection);
      if (postDtos.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(postDtos, HttpStatus.OK);

  }
  @GetMapping(PathConstants.API_V1+PathConstants.API_PUBLIC+"post/{postId}")
  public ResponseEntity<PostDto> getPostById(@PathVariable Long postId){
        Optional<Post> post= postService2.getPostById(postId);
        if(post.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(new PostDto(post.get()));
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

    @GetMapping(PathConstants.API_V1+"user/posts/me")
    public ResponseEntity<Page<PostDto>> findPostsByUser(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size,
                                                         @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                                         @RequestParam(defaultValue = "desc") String sortDirection,
                                                         Principal principal ){
      Long userId= utilService.getUserFromPrincipal(principal).getId();

     Page<Post>  postsPage= postService2.findPostsByUserId(userId,page,size,sortBy,sortDirection);

     if (postsPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }
     // Get the content (posts) from the Page object
        Page<PostDto> postDtos = postsPage.map(PostDto::new);


        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }


    @GetMapping(PathConstants.API_V1+"/user/saved")
    public ResponseEntity<Page<PostDto>>  getUserSavedPosts(
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestParam(defaultValue = "lastModifiedDate") 
                                                    @ValidPostSortBy String sortBy,
                                       @RequestParam(defaultValue = "desc") String sortDirection,
                                       Principal principal ){
       Long userId= utilService.getUserFromPrincipal(principal).getId();
       Page<Post>  postsPage= postService2.findUserSavedPosts(userId,page,size,sortBy,sortDirection);

       if (postsPage.isEmpty()) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
         // Get the content (posts) from the Page object
        Page<PostDto> postDtos = postsPage.map(PostDto::new);


        return new ResponseEntity<>(postDtos, HttpStatus.OK);
     }


     

    @GetMapping(PathConstants.API_V1+"/user/liked")
    public ResponseEntity<Page<PostDto>>  getUserLikedPosts(
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestParam(defaultValue = "lastModifiedDate") 
                                                    @ValidPostSortBy String sortBy,
                                       @RequestParam(defaultValue = "desc") String sortDirection,
                                       Principal principal ){
       Long userId= utilService.getUserFromPrincipal(principal).getId();
       Page<Post>  postsPage= postService2.findUserLikedPosts(userId,page,size,sortBy,sortDirection);

       if (postsPage.isEmpty()) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
         // Get the content (posts) from the Page object
        Page<PostDto> postDtos = postsPage.map(PostDto::new);


        return new ResponseEntity<>(postDtos, HttpStatus.OK);
     }

     /////////////////////////////

    @GetMapping(PathConstants.API_V1+PathConstants.API_PUBLIC+"post/saved/{postId}")
    public ResponseEntity<List<UserInfo>> getPostSavedBy(@PathVariable Long postId){
        return postService.findById(postId)
                .map(post -> ResponseEntity.ok(post.getSavedByUsers().stream()
                            .map(UserInfo::new)
                        .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(PathConstants.API_V1+PathConstants.API_PUBLIC+"post/liked/{postId}")
    public ResponseEntity<List<UserInfo>> getPostLikedBy(@PathVariable Long postId){
        return postService.findById(postId)
                .map(post -> ResponseEntity.ok(post.getLikedByUsers().stream()
                        .map(UserInfo::new)
                        .collect(Collectors.toList())))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping(path = PathConstants.API_V1+"post/create")
    public ResponseEntity<String> createPost(@RequestPart(value = "post",required = true) PostRequest postRequest,
    @RequestParam(required = false) List<MultipartFile> postImages,
        Principal principal) throws Exception{
        // convert Dao to Post
        Post newPost = new Post();
        newPost.setTitle(postRequest.getTitle());
        newPost.setContent(postRequest.getContent());
        newPost.setAnonymous(postRequest.isAnonymous());
        if (postImages!=null && !postImages.isEmpty() && postImages.get(0).getSize()>0){
            newPost.setPostImages(imageService.saveListImagePost(postImages));
        }
        /// set user for the post
        User user= utilService.getUserFromPrincipal(principal);
        //save post
        System.out.println(postImages.get(0).getSize());
        postService2.addPost(newPost,user,tagService.setTagsToPost(postRequest.getTags()),categoryService.SetPostCategory(postRequest.getCategory()));
       return ResponseEntity.ok("Post added successfully");
    }

    @PostMapping(path = PathConstants.API_V1+"post/edit/{postId}")
    public ResponseEntity<String> editPost(@PathVariable() Long postId, @RequestPart("post") PostRequest postRequest,
                                           @RequestParam(required = false) MultipartFile postImage,
                                           Principal principal) throws IOException {
        Optional<Post> postObj = postService.findById(postId);
        if(postObj.isPresent()){
            Post post = postObj.get();
            post.setAnonymous(postRequest.isAnonymous());
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            if(postRequest.isTagModifies()){
                post.removeTags();
                Set<Tag> tags= tagService.setTagsToPost(postRequest.getTags());
            }
            if(postRequest.isCategoryModifies()){
            post.getCategory().removePostFromCategoryById(postId);
            Category category = categoryService.SetPostCategory(postRequest.getCategory());
            category.addPostToCategory(post);
            post.setCategory(category);
            }
            if (postRequest.isImagesModifies()){
                imageService.removeImagesFromStorage(post);
                imageService.removeImagesFromPost(post);
            }
            postService2.updatePost(post);

            return ResponseEntity.ok("Post updated successfully");
        }
         return ResponseEntity.badRequest().body("Post was not updated successfully");
    }

    @DeleteMapping(path = PathConstants.API_V1+"post/{postId}/delete")
    public ResponseEntity<String> removePost(@PathVariable Long postId,
                                             Principal principal) throws Exception{
      Optional<Post> post = postService.findById(postId);
      if(post.isPresent()){
          if(!post.get().getPostImages().isEmpty()){
              imageService.removeImagesFromStorage(post.get());
              imageService.removeImagesFromPost(post.get());
          }

        postService2.deletePost(post.get());
        return ResponseEntity.ok("Post removed successfully");
      }
      return ResponseEntity.badRequest().body("Post was not removed successfully, please try again");
    }


    @PostMapping(path = PathConstants.API_V1+"post/{postId}/save")
    public ResponseEntity<String> savePost(@PathVariable Long postId,Principal principal) throws Exception{
      User user= utilService.getUserFromPrincipal(principal);
      Optional<Post> post = postService.findById(postId);
      if(post.isPresent()){

        postService.savePost(post.get(), user);

      return ResponseEntity.ok("Post saved successfully");
      }
      return ResponseEntity.badRequest().body("Post was not saved successfully");
    }

    @PostMapping(path = PathConstants.API_V1+"post/{postId}/unsave")
    public ResponseEntity<String> unSavePost(@PathVariable Long postId,Principal principal) throws Exception{
        User user= utilService.getUserFromPrincipal(principal);
      Optional<Post> post = postService.findById(postId);
      if(post.isPresent()){

        postService2.unsavePost(user, post.get());

      return ResponseEntity.ok("Post unsaved successfully");
      }
      return ResponseEntity.badRequest().body("Post was not unsaved successfully");
    }

  
    
    @PostMapping(path = PathConstants.API_V1+"post/{postid}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postid,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).orElseThrow();
      Optional<Post> post = postService2.getPostById(postid);
      if(post.isPresent()){
        postService2.likePost( user,post.get());
        /// notify only if user is not the owner
          if(!Objects.equals(post.get().getUser().getEmail(), principal.getName())){
              notificationService.createNotification(post.get(),null,user,null, NotificationType.POST_LIKE);
          }
      return ResponseEntity.ok("Post liked successfully");
      }
      return ResponseEntity.badRequest().body("Post was not liked successfully");
    }

    @PostMapping(path = PathConstants.API_V1+"post/{postId}/unlike")
    public ResponseEntity<String> unlikePost(@PathVariable Long postId,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).get();
      Optional<Post> post = postService2.getPostById(postId);
      if(post.isPresent()){

        postService2.unlikePost(user, post.get());

      return ResponseEntity.ok("Post unliked successfully");
      }
      return ResponseEntity.badRequest().body("Post was not unliked successfully");
    }






}
