package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.postRelated.Post;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends BaseRepository<Post, Long> {

   // @Query("SELECT * FROM posts p WHERE p.user_id=:userId")    
   Optional<Post> findPostByIdAndIsArchivedFalseAndIsPublic(Long postid,boolean isPublic);


   Page<Post> findPostsByUserIdAndIsArchivedFalseAndIsPublic(Long postid, Pageable pageable,boolean isPublic);
    Page<Post> findByListTagsTagNameAndIsArchivedFalseAndIsPublic(String tagName,Pageable pageable,boolean isPublic);
    Page<Post> findByCategoryCategoryAndIsArchivedFalseAndIsPublic(String category, Pageable pageable,boolean isPublic);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN p.listTags t LEFT JOIN p.category c WHERE (t.tagName = :tagName OR c.category = :category) AND p.isArchived = false AND p.isPublic = true")
    Page<Post> findByListTagsTagNameOrCategoryCategoryAndIsArchivedFalseAndIsPublicTrue(@Param("tagName") String tagName, @Param("category") String category, Pageable pageable);

}
