package com.example.springsocial.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.ImageRepo;
import com.example.springsocial.repository.PostRepo;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private GoogleCloudService googleCloudService;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserService userService;

    
    public Post createNewPost(Post post){  
        return postRepo.save(post);
    }

    public Post updatePost(Post post){
        return postRepo.save(post);
    }

    public Optional<Post> findById(Long id){
        return postRepo.findById(id);
    }

    public Page<Post> findByUserId(Long userId,Pageable pageable){
        return postRepo.findPostsByUserId(userId,pageable);
    }

    public Page<Post> findByTag(String tag,Pageable pageable){
        return postRepo.findByListTagsTagName(tag,pageable);
    }

    public Page<Post> findByCategory(String category,Pageable pageable){
        return postRepo.findByCategoryCategory(category,pageable);
    }

    public Page<Post> findPostsByTagOrCategory(String tag,String category,Pageable pageable){
        return postRepo.findByListTagsTagNameOrCategoryCategory(tag,category,pageable);
    }

    
    public boolean isPostUserMatch(User user,Post post){

        if(post.getUser()==user && user.getPosts().contains(post)){
            return true;
        }
        return false;
    }

    public void removePostById(Long id){
         postRepo.deleteById(id);
    }

    public void removePost(Post post,User user) throws Exception{
        user.removePostFromPostsList(post);
        post.removePostFromSavedLists(post);
        postRepo.delete(post);
    }

    public void savePost(Post post,User user) throws Exception{
        user.savePost(post);
        post.getSavedByUsers().add(user);
        userService.updateUser(user);
        postRepo.save(post);
    }

    public void removePostFromUserSavedList(User user,Post post) throws Exception{
        user.unsavePost(post);
        post.removePostfromUserSavedList(post, user);
        userService.updateUser(user);
        postRepo.save(post);
    }


}
