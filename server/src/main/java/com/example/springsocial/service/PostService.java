package com.example.springsocial.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.repository.PostRepo;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private GoogleCloudService googleCloudService;

    @Autowired
    private ImageRepo imageRepo;
    
    public Post createNewPost(Post post){
        
        return postRepo.save(post);
    }

    public Post updatePost(Post post){
        return postRepo.save(post);
    }

    public Optional<Post> findById(Long id){
        return postRepo.findById(id);
    }

    public List<Post> findByUserId(Long userId){
        return postRepo.findPostsByUserId(userId);
    }

}
