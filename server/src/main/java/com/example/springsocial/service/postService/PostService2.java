package com.example.springsocial.service.postService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.util.ProjectUtil;
import com.example.springsocial.validator.permessions.PostOwner;
import com.example.springsocial.util.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.UtilService;
import com.example.springsocial.specification.PostSpecification;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

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

    //@Cacheable(value = "feed")
    public List<PostDto>getFeedByCursor(String category,String tagName,String cursor,
                                 int pageSize,String sortBy,String sortDirection) throws ParseException {
        Specification<Post> spec = userSpec(category, tagName);
        if (cursor != null) {
            String[] cursorParts = cursor.split(",");
            String cursorFieldValue = cursorParts[0].split(":",2)[1];  // Extract the field value from the cursor
            Long cursorId = Long.parseLong(cursorParts[1].split(":")[1]);  // Extract the post ID from the cursor
            if ("lastModifiedDate".equals(sortBy)) {
                Date cursorDate = ProjectUtil.convertStringToDate(cursorFieldValue);

            spec = spec.and((root, query, criteriaBuilder) -> {
                Predicate cursorPredicate = sortDirection.equalsIgnoreCase("ASC")
                        ? criteriaBuilder.greaterThan(root.get(sortBy), cursorDate)
                        : criteriaBuilder.lessThan(root.get(sortBy), cursorDate);

                Predicate idPredicate = sortDirection.equalsIgnoreCase("ASC")
                        ? criteriaBuilder.greaterThan(root.get("id"), cursorId)
                        : criteriaBuilder.lessThan(root.get("id"), cursorId);

                return criteriaBuilder.or(cursorPredicate, idPredicate);
            });
        } else {
            Integer cursorCountValue = Integer.parseInt(cursorFieldValue);  // Parse integer for count-based sorting

            spec = spec.and((root, query, criteriaBuilder) -> {
                Predicate cursorPredicate = sortDirection.equalsIgnoreCase("ASC")
                        ? criteriaBuilder.greaterThan(root.get(sortBy), cursorCountValue)
                        : criteriaBuilder.lessThan(root.get(sortBy), cursorCountValue);

                Predicate idPredicate = sortDirection.equalsIgnoreCase("ASC")
                        ? criteriaBuilder.greaterThan(root.get("id"), cursorId)
                        : criteriaBuilder.lessThan(root.get("id"), cursorId);

                return criteriaBuilder.or(cursorPredicate, idPredicate);
            });
        }
    }



        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy); // Simple sort creation
        Pageable pageable = PageRequest.of(0, pageSize, sort); // Always page 0 for cursor-based
        Page<Post> postsPage = postRepo.findAll(spec, pageable);


        return postsPage.getContent().stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }



    @Cacheable(value = "feed")
     public Page<PostDto>getFeedPosts(String category,String TagName,
                                       int pageNo,int pageSize,String sortBy,String sortDirection){
        Specification<Post> spec = userSpec(category, TagName);




       Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);

       Page<Post>  postsPage= postRepo.findAll(spec,paging);

       return postsPage.map(PostDto::new);
    }

    public Optional<Post> getPostById(Long postId){
        return postRepo.findById(postId);
    }

    @CacheEvict(cacheNames="feed", allEntries=true)
    public void addPost(Post post, User user, Set<Tag> tags, Category category){
        post.setUser(user);
        category.getPosts().add(post);
        post.setCategory(category);
        post.setListTags(tags);

        if(user.addPost(post)){
            postRepo.save(post);
        }
    }

    public Optional<Post> findPostById(Long postId){
         return postRepo.findById(postId);

    }

    @PostOwner
    @CacheEvict(cacheNames="feed", allEntries=true)
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



}
