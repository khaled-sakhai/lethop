package com.example.springsocial.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.dto.post.PostRequest;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.service.CategoryService;
import com.example.springsocial.service.GoogleCloudService;
import com.example.springsocial.service.PostService;
import com.example.springsocial.service.TagService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.util.Constants;
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

  /// feed page (category= good)
  @GetMapping("api/v1/public/posts")
  public ResponseEntity<List<PostDto>> getPostsByTagAndCategory(@RequestParam(required = false) String category,
  @RequestParam(required = false) String tag,
  @RequestParam(defaultValue = "0") int page,
  @RequestParam(defaultValue = "20") int size,
  @RequestParam(defaultValue = "desc") String sortDirection){
// Validate the page size using the constant
    if (size > Constants.MAX_PAGE_SIZE) {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    // Category categoryDb = categoryService.findByCategory(category.toLowerCase());

    // Optional<Tag> tagDb = tagService.findByTag(tag);


   

         // Define the sorting direction and property
          Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "lastModifiedDate");

          // Create a Pageable object with sorting and paging parameters
          Pageable pageable = PageRequest.of(page, size, sort);
          
          // Retrieve paginated and sorted posts
          Page<Post> postsPage = postService.findPostsByTagOrCategory(tag, category, pageable);

          // Get the content (posts) from the Page object
          // return as post objects (full fields)
        //  List<Post> posts = postPage.getContent();

         if (postsPage.isEmpty()) {
         // Handle case where no posts are found based on the provided criteria
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }

         System.out.println(postsPage.getNumberOfElements());

          // Convert the List<Post> to List<PostDto>
          List<PostDto> postDtos = postsPage.getContent().stream()
          .map(PostDto::new)
          .collect(Collectors.toList());
          System.out.println(postDtos.size());

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

    @GetMapping("api/v1/public/post/{postid}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postid){
      Optional<Post> post= postService.findById(postid);
      if(post.isPresent()){
        return ResponseEntity.status(HttpStatus.OK).body(new PostDto(post.get()));
      }
      else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    


    @PostMapping(path = "api/v1/post/create")
    public ResponseEntity<String> createPost(@RequestPart("post") PostRequest postRequest,
    @RequestParam(required = false) MultipartFile postImage,
        Principal principal) throws Exception{
        // convert Dao to Post
        Post newPost = new Post();
        newPost.setTitle(postRequest.getTitle());
        newPost.setContent(postRequest.getContent());
        //tag

        String [] items = postRequest.getTags().split("\\s*,\\s*");
        Set<Tag> tags = new HashSet<>();
        for(String tag: items){
            Tag tagDb = tagService.saveTag(new Tag(tag));
            tags.add(tagDb);
        }
        newPost.setListTags(tags);
        /// post settings
        newPost.setPublic(postRequest.isPublic());
        newPost.setAnonymous(postRequest.isAnonymous());
        ///category
        Category categoryObj = new Category(postRequest.getCategory());
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
    public ResponseEntity<String> editPost(@PathVariable(required = true) Long postid,@RequestPart("post") PostRequest postRequest, 
                                           @RequestParam(required = false) MultipartFile postImage,
                                           Principal principal){
        Optional<Post> postObj = postService.findById(postid);
        if(postObj.isPresent()){
            Post post = postObj.get();
            User user= userService.findByEmail(principal.getName()).get();
            if(postService.isPostUserMatch(user, post)){
                   ///post settings
                   post.setPublic(postRequest.isPublic());
                   post.setAnonymous(postRequest.isAnonymous());
                   //title
                   post.setTitle(postRequest.getTitle());
                   //// content
                   post.setContent(postRequest.getContent());
                   //tags
                   if(postRequest.isTagModifies()){
                     Set<Tag> oldTags= post.getListTags();
                     String [] itemsTags = postRequest.getTags().split("\\s*,\\s*");
                     Set<Tag> tags = new HashSet<>();
                     for(String tag: itemsTags){
                        Tag tagDb = tagService.saveTag(new Tag(tag));
                        tags.add(tagDb);
                      }

                    post.setListTags(tags);
                   }
                   // update category
                   if(postRequest.isCategoryModifies() &&
                   !postRequest.getCategory().toLowerCase().equalsIgnoreCase(post.getCategory().getCategory())){

                    post.getCategory().removePostFromCategoryById(postid);

                    Category categoryObj = new Category(postRequest.getCategory());
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

  


    




}
