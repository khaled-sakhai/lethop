package com.example.springsocial.service.postService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.User;
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
import com.example.springsocial.specification.PostSpecification;

@Service
public class PostService2 {
    
    @Autowired
    private PostRepo postRepo;


     public Page<Post> getFeedPosts(String category,String TagName,
                                       int pageNo,int pageSize,String sortBy,String sortDirection){

        Specification<Post> spec = userSpec(category, TagName);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        
        return postRepo.findAll(spec,paging);
    }

    public Post getPostById(Long postId){
        Optional<Post> post = postRepo.findById(postId);
        return post.orElseThrow();
    }

    public void addPost(Post post, User user, Set<Tag> tags, Category category, List<Image> images){
        post.setUser(user);
        category.getPosts().add(post);
        post.setCategory(category);
        post.setListTags(tags);

        if(user.addPost(post)){
            postRepo.save(post);
        }
    }


    private Specification<Post> userSpec(String category,String TagName){
        return Specification
        .where(PostSpecification.postWithCategory(category, Constants.AllowedCategory[0]))
              .and(PostSpecification.postWithTag(TagName, Constants.AllowedTags[0]));
    }

    private Specification<Post> adminSpec(boolean isDelete,boolean isAnonymous){
        return Specification.where(
            PostSpecification.isDeleted(isDelete)
        ).and(PostSpecification.isAnonymous(isDelete))
        ;
    }


}
