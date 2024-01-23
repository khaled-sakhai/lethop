package com.example.springsocial.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.repository.CategoryRepo;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepo categoryRepo;

    public Category saveCategory(Category category){
        Optional<Category> categoryFromDB = categoryRepo.findByCategory(category.getCategory());
        if(!categoryFromDB.isPresent()){
            return categoryRepo.save(category);
        }
        else{
            return categoryFromDB.get();
        }
    }
}
