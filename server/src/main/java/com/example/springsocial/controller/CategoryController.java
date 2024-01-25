package com.example.springsocial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.service.CategoryService;

@RestController
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("api/v1/public/posts/{category}")
    public ResponseEntity<List<Post>> findPostsByCategory(@PathVariable String category){
        // we can also do posts.findbyCategory(Category category)
        List<Post> posts = categoryService.findPostsByCategory(category);
        
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
