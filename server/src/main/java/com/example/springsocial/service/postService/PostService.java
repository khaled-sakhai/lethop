package com.example.springsocial.service.postService;

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
import com.example.springsocial.service.GoogleCloudService;
import com.example.springsocial.service.UserService;

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

    public Optional<Post> findAnyPostById(Long id){
        return postRepo.findById(id);
    }

    
    public Optional<Post> findById(Long id){
        return postRepo.findPostByIdAndIsArchivedFalseAndIsPublic(id,true);
    }

    public Page<Post> findByUserId(Long userId,Pageable pageable){
        return postRepo.findPostsByUserIdAndIsArchivedFalseAndIsPublic(userId,pageable,true);
    }

    public Page<Post> findByTag(String tag,Pageable pageable){
        return postRepo.findByListTagsTagNameAndIsArchivedFalseAndIsPublic(tag,pageable,true);
    }

    public Page<Post> findByCategory(String category,Pageable pageable){
        return postRepo.findByCategoryCategoryAndIsArchivedFalseAndIsPublic(category,pageable,true);
    }

    //feed page
    public Page<Post> findPostsByTagOrCategory(String tag,String category,Pageable pageable){
        return postRepo.findByListTagsTagNameOrCategoryCategoryAndIsArchivedFalseAndIsPublicTrue(tag,category,pageable);
    }

    public boolean isPostedByUser(User user,Post post){
        if(post.getUser()==user && user.getPosts().contains(post)){
            return true;
        }
        return false;
    }


    public void removePostById(Long id){
         postRepo.deleteById(id);
    }

    public void archivePost(Post post,User user){
        if(isPostedByUser(user, post)){
        post.setArchived(true);
        postRepo.save(post);
        }
       
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

    public void unsavePost(User user,Post post) throws Exception{
        if(!user.getSavedPosts().contains(post)){     
            throw new Exception("This post is not on your saved posts list, you can't unsave what's not saved");
        }
        user.getSavedPosts().removeIf(p->p.getId()==post.getId());        
        post.getSavedByUsers().removeIf(u->u.getId()==user.getId());

        user.updateSavedCounter();
        post.updateSavedCount();
        userService.updateUser(user);
        postRepo.save(post);
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
        if(!user.getLikedPosts().contains(post)){
            throw new Exception("you're already unliked this post, no need to unlinke it again");
        }
        user.getLikedPosts().removeIf(p-> p.getId()==post.getId());
        post.getLikedByUsers().removeIf(u-> u.getId()==user.getId());
        user.updateLikedCounter();
        post.updateLikedCount();
        userService.updateUser(user);
        postRepo.save(post);
    }

    
    public void makePostPublic(Post post,User user) throws Exception{
        if(isPostedByUser(user,post) && !post.isPublic()){
            post.setPublic(true);
        }
        throw new Exception("failed, please try again");
    }

    public void makePostNonPublic(Post post,User user) throws Exception{
        if(isPostedByUser(user,post)  && post.isPublic()){
            post.setPublic(false);
        }
        throw new Exception("failed, please try again");
    }



}
