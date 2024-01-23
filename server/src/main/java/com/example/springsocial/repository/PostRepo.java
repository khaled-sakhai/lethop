package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.postRelated.Post;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends BaseRepository<Post, Long> {

   // @Query("SELECT * FROM posts p WHERE p.user_id=:userId")    
    List<Post> findPostsByUserId(@Param("userId") Long userId);
}
