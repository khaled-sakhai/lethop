package com.example.springsocial.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.springsocial.service.ImageService;
import com.example.springsocial.service.UtilService;
import com.example.springsocial.service.postService.PostService2;
import com.example.springsocial.validator.validators.ValidPostSortBy;
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
import com.example.springsocial.service.GoogleCloudService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.CategoryService;
import com.example.springsocial.service.postService.PostService;
import com.example.springsocial.service.postService.TagService;
import com.example.springsocial.util.Constants;


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

  @Autowired
  private GoogleCloudService googleCloudService;

  @Autowired
  private TagService tagService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private UtilService utilService;
    @GetMapping("/api/v1/public/search")

    public  List<Post>  searchPosts(@RequestParam String searchText) {
        return postService2.searchPosts(searchText);
    }

  @GetMapping("api/v1/public/feed")
  public  ResponseEntity<List<PostDto>> getFeed(
  @RequestParam(required = false) String category,
  @RequestParam(required = false) String tag,
  @RequestParam(defaultValue = "0") int page,
  @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
  @RequestParam(defaultValue = "20") int size,
  @RequestParam(defaultValue = "desc") String sortDirection
  ){
      Page<Post>  postsPage= postService2.getFeedPosts(category, tag, page, size, sortBy, sortDirection);
      if (postsPage.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      // Get the content (posts) from the Page object
      List<PostDto> postDtos = postsPage.getContent().stream()
              .map(PostDto::new)
              .collect(Collectors.toList());

      return new ResponseEntity<>(postDtos, HttpStatus.OK);

  }
  @GetMapping("api/v1/public/post/{postId}")
  public ResponseEntity<PostDto> getPostById(@PathVariable Long postId){
        Optional<Post> post= postService2.findPostById(postId);
        if(post.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(new PostDto(post.get()));
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

    @GetMapping("api/v1/public/user/posts/me")
    public ResponseEntity<List<PostDto>> findPostsByUser(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "20") int size,
                                                         @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                                         @RequestParam(defaultValue = "desc") String sortDirection,Principal principal ){
      Long userId= utilService.getUserFromPrincipal(principal).getId();

     Page<Post>  postsPage= postService2.findPostsByUserId(userId,page,size,sortBy,sortDirection);

     if (postsPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }
     // Get the content (posts) from the Page object
     List<PostDto> postDtos = postsPage.getContent().stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());

     return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }


  /// feed page (category= good)
  @GetMapping("api/v1/public/posts/category/{category}")
  public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable String category,
  @RequestParam(defaultValue = "0") int page,
  @RequestParam(defaultValue = "20") int size,
  @RequestParam(defaultValue = "desc") String sortDirection){
// Validate the page size using the constant
    if (size > Constants.MAX_PAGE_SIZE) {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    Category categoryDb = categoryService.findByCategory(category.toLowerCase());

    if(categoryDb !=null){

        // Define the sorting direction and property
          Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "lastModifiedDate");

          // Create a Pageable object with sorting and paging parameters
          Pageable pageable = PageRequest.of(page, size, sort);
          
          // Retrieve paginated and sorted posts
          Page<Post> postsPage = postService.findByCategory(category.toLowerCase(), pageable);

          // Get the content (posts) from the Page object
          List<PostDto> postDtos = postsPage.getContent().stream()
          .map(PostDto::new)
          .collect(Collectors.toList());

          return new ResponseEntity<>(postDtos, HttpStatus.OK);


    }
    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  }

  @GetMapping("api/v1/public/posts/tag/{tag}")
    public ResponseEntity<List<PostDto>> getPostsByTag(@PathVariable String tag,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "desc") String sortDirection ){

      if (size > Constants.MAX_PAGE_SIZE) {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

      Optional<Tag> tagDb = tagService.findByTag(tag);

      if(tagDb.isPresent()){
         // Define the sorting direction and property
         Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "lastModifiedDate");

         // Create a Pageable object with sorting and paging parameters
         Pageable pageable = PageRequest.of(page, size, sort);
         
         // Retrieve paginated and sorted posts
         Page<Post> postsPage = postService.findByTag(tag.toLowerCase(), pageable);
         System.out.println(postsPage.getNumberOfElements());

         // Get the content (posts) from the Page object
         List<PostDto> postDtos = postsPage.getContent().stream()
         .map(PostDto::new)
         .collect(Collectors.toList());

         return new ResponseEntity<>(postDtos, HttpStatus.OK);
      }
      
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("api/v1/public/user/{userid}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Long userid,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "desc") String sortDirection ){
      if (size > Constants.MAX_PAGE_SIZE) {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    Optional<User> user = userService.findById(userid);
    if(user.isPresent()){
        // Define the sorting direction and property
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "lastModifiedDate");

        // Create a Pageable object with sorting and paging parameters
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Retrieve paginated and sorted posts
        Page<Post> postsPage = postService.findByUserId(userid, pageable);

        // Get the content (posts) from the Page object
        List<PostDto> postDtos = postsPage.getContent().stream()
        .map(PostDto::new)
        .collect(Collectors.toList());

        return new ResponseEntity<>(postDtos, HttpStatus.OK);
      }
      
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("api/v1/public/post/saved/{postid}")
    public Set<User> getPostSavedBy(@PathVariable Long postid){
      Optional<Post> post = postService.findById(postid);
      if(post.isPresent()){
        return post.get().getSavedByUsers();
      }
      return null;
    }


/// post settings // only for authenticated users

    @PostMapping(path = "api/v1/post/create")
    public ResponseEntity<String> createPost(@RequestPart(value = "post",required = true) PostRequest postRequest,
    @RequestParam(required = false) List<MultipartFile> postImages,
        Principal principal) throws Exception{
        // convert Dao to Post
        Post newPost = new Post();
        newPost.setTitle(postRequest.getTitle());
        newPost.setContent(postRequest.getContent());
        newPost.setAnonymous(postRequest.isAnonymous());
        if (postImages!=null && !postImages.isEmpty()){
            newPost.setPostImages(imageService.saveListImagePost(postImages));

        }
        /// set user for the post
        User user= utilService.getUserFromPrincipal(principal);
        //save post
        postService2.addPost(newPost,user,tagService.setTagsToPost(postRequest.getTags()),categoryService.SetPostCategory(postRequest.getCategory()));
       return ResponseEntity.ok("Post added successfully");
    }

    @PostMapping(path = "api/v1/post/edit/{postId}")
    public ResponseEntity<String> editPost(@PathVariable() Long postId, @RequestPart("post") PostRequest postRequest,
                                           @RequestParam(required = false) MultipartFile postImage,
                                           Principal principal){
        Optional<Post> postObj = postService.findById(postId);
        if(postObj.isPresent()){
            Post post = postObj.get();
            User user= utilService.getUserFromPrincipal(principal);
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
                imageService.removeImagesFromPost(post);
            }
            postService2.updatePost(post);

            return ResponseEntity.ok("Post updated successfully");
        }
         return ResponseEntity.badRequest().body("Post was not updated successfully");
    }

    @DeleteMapping(path = "api/v1/post/{postId}/delete")
    public ResponseEntity<String> removePost(@PathVariable Long postId,
                                             Principal principal) throws Exception{
       // User user= utilService.getUserFromPrincipal(principal);

      Optional<Post> post = postService.findById(postId);
      if(post.isPresent()){
        postService2.deletePost(post.get());
        return ResponseEntity.ok("Post removed successfully");
      }
      return ResponseEntity.badRequest().body("Post was not removed successfully, please try again");
    }


    @PostMapping(path = "api/v1/post/{postId}/save")
    public ResponseEntity<String> savePost(@PathVariable Long postId,Principal principal) throws Exception{
        User user= utilService.getUserFromPrincipal(principal);
      Optional<Post> post = postService.findById(postId);
      if(post.isPresent()){

        postService.savePost(post.get(), user);

      return ResponseEntity.ok("Post saved successfully");
      }
      return ResponseEntity.badRequest().body("Post was not saved successfully");
    }

    @PostMapping(path = "api/v1/post/{postId}/unsave")
    public ResponseEntity<String> unSavePost(@PathVariable Long postId,Principal principal) throws Exception{
        User user= utilService.getUserFromPrincipal(principal);
      Optional<Post> post = postService.findById(postId);
      if(post.isPresent()){

        postService.unsavePost(user, post.get());

      return ResponseEntity.ok("Post unsaved successfully");
      }
      return ResponseEntity.badRequest().body("Post was not unsaved successfully");
    }

  
    @PostMapping(path = "api/v1/post/{postid}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postid,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).get();
      Optional<Post> post = postService.findById(postid);
      if(post.isPresent()){

        postService.likePost( user,post.get());

      return ResponseEntity.ok("Post liked succesfully");
      }
      return ResponseEntity.badRequest().body("Post was not liked succesfully");     
    }

    @PostMapping(path = "api/v1/post/{postid}/unlike")
    public ResponseEntity<String> unlikePost(@PathVariable Long postid,Principal principal) throws Exception{
      User user= userService.findByEmail(principal.getName()).get();
      Optional<Post> post = postService.findById(postid);
      if(post.isPresent()){

        postService.unlikePost(user, post.get());

      return ResponseEntity.ok("Post unliked succesfully");
      }
      return ResponseEntity.badRequest().body("Post was not unliked succesfully");     
    }



}
