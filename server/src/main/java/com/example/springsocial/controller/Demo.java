package com.example.springsocial.controller;

import com.example.springsocial.dto.comments.CommentRequest;
import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.CommentReplayService;
import com.example.springsocial.service.postService.PostService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
public class Demo {
    
@Autowired
private PostService postService;
    /// just to test methods
  @Autowired
    private UserService userService;
    @Autowired
    private CommentReplayService commentReplayService;
    
    @GetMapping("/xx")
    public String getsmth(){
        System.out.println("hoklla");
        return "Springoo - this endpoint is not authenticated";
    }

     @GetMapping("/aa")
    public String getsmth1(){
        System.out.println("hoklla");
        return "Springoo authenticated endpoint";
    }

    @GetMapping("api/v1/public/posty")
    public List<Post> getposty(){
        return postService.getbyLikes();
    }

    @GetMapping("/api/v1/public/search")
    public  List<Post>  searchPosts(@RequestParam String searchText) {
        return postService.searchPosts(searchText);
    }

    @PostMapping(path = "api/v1/post/{postid}/comment")
    public String addCommentToPost(
        @PathVariable Long postid,
        @RequestBody CommentRequest commentRequest,
        Principal principal){
     User user= userService.findByEmail(principal.getName()).get();

    Comment comment = new Comment();
    comment.setContent(commentRequest.getContent());
    comment.setUser(user);
    commentReplayService.addCommentForPost(postid, comment, user);
    
    return "Comment has been added succefuly";
        
    }
    @GetMapping("/api/v1/public/comment/{postid}")
    public  Set<Comment>  findCommentsByPostId(@PathVariable Long postid) {
        return commentReplayService.getPostComments(postid);
    }

    @GetMapping("/api/v1/public/comment/id/{commentid}")
    public  Comment  findCommentById(@PathVariable Long commentid) {
        return commentReplayService.getCommentById(commentid);
    }
}
