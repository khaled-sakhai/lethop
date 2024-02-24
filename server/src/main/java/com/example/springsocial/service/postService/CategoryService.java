package com.example.springsocial.service.postService;

import java.util.List;
import java.util.Optional;

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

    public Category findByCategory(String category){
        Optional<Category> categoryFromDB = categoryRepo.findByCategory(category);
        
        if (categoryFromDB.isPresent()) {
            return categoryFromDB.get();
        }
        return null;
    }

    public List<Post> findPostsByCategory(String category){
        Category categoryObj = findByCategory(category);
        if(categoryObj!=null){
            List<Post> publicPosts= categoryObj.getPosts();
            return publicPosts;
        }
        return null;
    }
}
