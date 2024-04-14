package com.example.springsocial.service.postService;

import java.util.List;
import java.util.Optional;

import com.example.springsocial.specification.PostSpecification;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.springsocial.entity.postRelated.Post;

import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepo postRepo;



    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserService userService;
  


    
    public List<Post> getbyLikes(){
        Specification<Post> spec = Specification.where(PostSpecification.hasLikesInRange(0,100));
        return postRepo.findAll(spec);
    }



    
    public Optional<Post> findById(Long id){
        return postRepo.findPostById(id);
    }





    public void savePost(Post post,User user) throws Exception{
        if(user.getSavedPostsCount()>=10){
          throw new Exception("You can't save more than 10 posts, remove posts from your list to be able to save this post");
        }
        if(user.getSavedPosts().contains(post)){
              throw new Exception("This post is already in your saved list");
        }

        user.getSavedPosts().add(post);
        user.updateSavedCounter();
        post.getSavedByUsers().add(user);
        post.updateSavedCount();
        userService.updateUser(user);
        postRepo.save(post);
    }





}
