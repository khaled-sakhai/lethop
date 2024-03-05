package com.example.springsocial.service.postService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.validator.permessions.PostOwner;
import com.example.springsocial.util.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.UtilService;
import com.example.springsocial.specification.PostSpecification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class PostService2 {
    
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private UtilService utilService;
    @PersistenceContext
    private EntityManager entityManager;

     public Page<Post> getFeedPosts(String category,String TagName,
                                       int pageNo,int pageSize,String sortBy,String sortDirection){
        Specification<Post> spec = userSpec(category, TagName);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return postRepo.findAll(spec,paging);
    }

    public Optional<Post> getPostById(Long postId){
        Optional<Post> post = postRepo.findById(postId);
        return post;
    }

    public void addPost(Post post, User user, Set<Tag> tags, Category category){
        post.setUser(user);
        category.getPosts().add(post);
        post.setCategory(category);
        post.setListTags(tags);

        if(user.addPost(post)){
            postRepo.save(post);
        }
    }


    @PostOwner
    //soft delete
    public void deletePost(Post post){
        postRepo.delete(post);
    }

    @PostOwner
    public void updatePost(Post post){
        postRepo.save(post);
    }


    @PostOwner
    public Page<Post> findPostsByUserId(Long userId,int pageNo,int pageSize,String sortBy,String sortDirection){
        Specification<Post> spec = Specification.where(PostSpecification.postByUserId(userId));
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
         return postRepo.findAll(spec,paging);
    }
    
    public Page<Post> findUserSavedPosts(Long userId,int pageNo,int pageSize,String sortBy,String sortDirection){
        Specification<Post> spec = Specification.where(PostSpecification.savedByUser(userId));
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
         return postRepo.findAll(spec,paging);
    }


    public Page<Post> findUserLikedPosts(Long userId,int pageNo,int pageSize,String sortBy,String sortDirection){
        Specification<Post> spec = Specification.where(PostSpecification.likedByUser(userId));
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
         return postRepo.findAll(spec,paging);
    }
    
    public void savePost(Post post,User user) throws Exception{
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


    public void unsavePost(User user,Post post) throws Exception{
        if(user.getSavedPosts().contains(post)){
        user.getSavedPosts().removeIf(p->p.getId()==post.getId());        
        post.getSavedByUsers().removeIf(u->u.getId()==user.getId());
        user.updateSavedCounter();
        post.updateSavedCount();
        userService.updateUser(user);
        postRepo.save(post);
        }
        else{
            throw new Exception("This post is not on your saved posts list, you can't unsave what's not saved");
        }
    }

    public void likePost(User user,Post post) throws Exception{
        if(user.getLikedPosts().contains(post)){
            throw new Exception("you've already liked this post");
        }
        user.getLikedPosts().add(post);
        post.getLikedByUsers().add(user);;

        user.updateLikedCounter();
        post.updateLikedCount();
        userService.updateUser(user);
        postRepo.save(post);
    }

    public void unlikePost(User user,Post post) throws Exception{
        if(user.getLikedPosts().contains(post)){
        user.getLikedPosts().removeIf(p-> p.getId()==post.getId());
        post.getLikedByUsers().removeIf(u-> u.getId()==user.getId());
        user.updateLikedCounter();
        post.updateLikedCount();
        userService.updateUser(user);
        postRepo.save(post);            
        }
        else{throw new Exception("you're already unliked this post, no need to unlinke it again");}
       }

    ///////helper

    private Specification<Post> userSpec(String category,String TagName){
        return Specification
        .where(PostSpecification.postWithCategory(category, Constants.AllowedCategory[0]))
              .and(PostSpecification.postWithTag(TagName, Constants.AllowedTags[0]));
    }

    private Specification<Post> adminSpec(boolean isDelete,boolean isAnonymous,Long userId){
        return Specification.where(
            PostSpecification.isDeleted(isDelete)
        ).and(PostSpecification.isAnonymous(isDelete)).and(PostSpecification.postByUserId(userId))
        ;
    }


}
