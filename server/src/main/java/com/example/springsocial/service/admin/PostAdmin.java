package com.example.springsocial.service.admin;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.repository.PostRepo;
import com.example.springsocial.specification.PostSpecification;
import com.example.springsocial.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@AllArgsConstructor
public class PostAdmin {
    private final PostRepo postRepo;




    public Page<Post> findAll(Boolean isAnonymous,Long userId,Long postId,String category,String tag, int pageNo,int pageSize,String sortBy,String sortDirection){
        Specification<Post> spec= this.adminSpec(isAnonymous,userId,postId).and(this.userSpec(category,tag));
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);

        return postRepo.findAll(spec,paging);
    }

    public Page<Post> findDeleted(int pageNo,int pageSize,String sortBy,String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable paging = PageRequest.of(pageNo, pageSize, sort);
        return postRepo.findAllDeleted(paging);
    }

    public Optional<Post> findAnyPostById(long postId){
        return postRepo.findAnyPostById(postId);
    }
    public void finalDeleteById(Post post){
        post.getUser().getPosts().remove(post);
        post.getLikedByUsers().clear();
        post.getPostComments().clear();
        post.getSavedByUsers().clear();
        post.getListTags().clear();
        post.getCategory().removePostFromCategory(post);
        postRepo.deletePostById(post.getId());
    }
    private Specification<Post> userSpec(String category, String TagName){
        return Specification
                .where(PostSpecification.postWithCategory(category, Constants.AllowedCategory[0]))
                .and(PostSpecification.postWithTag(TagName, Constants.AllowedTags[0]));
    }

    private Specification<Post> adminSpec(Boolean isAnonymous,Long userId,Long postId){
        return Specification.where(PostSpecification.isAnonymous(isAnonymous)).and(PostSpecification.postByUserId(userId)).and(PostSpecification.postId(postId))
        ;
    }

}
