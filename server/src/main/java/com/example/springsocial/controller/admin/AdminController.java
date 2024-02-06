package com.example.springsocial.controller.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.service.CategoryService;
import com.example.springsocial.service.GoogleCloudService;
import com.example.springsocial.service.PostService;
import com.example.springsocial.service.TagService;
import com.example.springsocial.service.UserService;
import java.util.Arrays;

@RestController
public class AdminController {
    
    @Autowired
    private UserService userService;
        @Autowired
    private PostService postService;

    @Autowired
    private ImageRepo imageRepo;
 @Autowired
  private CategoryService categoryService;

  @Autowired
  private GoogleCloudService googleCloudService;

  @Autowired
  private TagService tagService;



   @GetMapping(value = "/api/admin/users")    
   public List<User> getAllUsers(Principal principal){
        return userService.findAll();
   }

    @GetMapping(value = "/api/admin/users/active/{provider}")    
    public List<User> getActiveByProvider(@PathVariable AuthProvider provider){
        return userService.findAllActiveUsers(provider);
    }

    @GetMapping(value = "/api/admin/users/unactive/{provider}")    
    public List<User> getNonActiveByProvider(@PathVariable AuthProvider provider){
        return userService.findAllNonActiveUsers(provider);
    }
    //// posts
    // fully remove a post
    
    // @DeleteMapping(path = "api/admin/post/{postid}/delete")
    // public ResponseEntity<String> removePost(@PathVariable Long postid,Principal principal) throws Exception{
    //   User user= userService.findByEmail(principal.getName()).get();
    //   Optional<Post> post = postService.findById(postid);
    //   if(post.isPresent() && post.get().getUser()==user){

    //     // handle image remove ...--to be added later
    //     Post postObj = post.get();
    //     postObj.getCategory().removePostFromCategoryById(postObj.getId());
    //     postService.removePost(postObj, user);
    //     return ResponseEntity.ok("Post removed succesfully");
    //   }
    
    //   return ResponseEntity.badRequest().body("Post was not removed succesfully");     
    // }


}

