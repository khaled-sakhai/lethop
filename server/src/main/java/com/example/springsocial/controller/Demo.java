package com.example.springsocial.controller;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.service.postService.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Demo {
    
@Autowired
private PostService postService;
    /// just to test methods
    
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

    @GetMapping("/search")
    public  List<Post>  searchPosts(@RequestParam String searchText) {
        return postService.searchPosts(searchText);
    }
}
