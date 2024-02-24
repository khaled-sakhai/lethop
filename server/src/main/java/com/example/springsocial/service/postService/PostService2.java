package com.example.springsocial.service.postService;

import java.security.Principal;

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

        Specification<Post> spec = userSpec(category, TagName, pageNo, pageSize, sortBy, sortDirection);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        
        return postRepo.findAll(spec,paging);
    }


    private Specification<Post> userSpec(String category,String TagName,int pageNo,int pageSize,String sortBy,String sortDirection){
        return Specification
        .where(PostSpecification.postWithCategory(category))
              .and(PostSpecification.postWithTag(TagName, "Motivation"));
    }

    private Specification<Post> adminSpec(boolean isDelete,boolean isAnonymous){
        return Specification.where(
            PostSpecification.isDeleted(isDelete)
        ).and(PostSpecification.isAnonymous(isDelete))
        ;
    }


}
