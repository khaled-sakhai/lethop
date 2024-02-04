package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.postRelated.Post;
import org.springframework.data.domain.Page;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends BaseRepository<Post, Long> {

   // @Query("SELECT * FROM posts p WHERE p.user_id=:userId")    
   Page<Post> findPostsByUserId(Long userId, Pageable pageable);
    Page<Post> findByListTagsTagName(String tagName,Pageable pageable);
    Page<Post> findByCategoryCategory(String category, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.listTags t LEFT JOIN p.category c WHERE t.tagName = :tagName OR c.category = :category")
    Page<Post> findByListTagsTagNameOrCategoryCategory(@Param("tagName") String tagName, @Param("category") String category, Pageable pageable);
    


    
}
