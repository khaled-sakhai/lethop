package com.example.springsocial.service.postService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.springsocial.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.repository.CategoryRepo;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepo categoryRepo;

    public Category saveCategory(Category category){
        Category categoryFromDB = findByCategory(category.getCategory());

        if(categoryFromDB == null){
            return categoryRepo.save(category);
        }
        else{
            return categoryFromDB;
        }
    }

    public Category SetPostCategory(String categoryString){
        if (categoryString==null || categoryString.isBlank() || !Arrays.asList(Constants.AllowedCategory).contains(categoryString.toLowerCase())){
            throw new IllegalArgumentException("Please use a valid category");
        }

        Optional<Category> categoryObj = categoryRepo.findByCategory(categoryString);
        return categoryObj.orElseThrow();
    }
    public Category findByCategory(String category){
        Optional<Category> categoryFromDB = categoryRepo.findByCategory(category);
        return categoryFromDB.orElse(null);
    }




}
