package com.example.springsocial.repository;

import org.springframework.stereotype.Repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Comment;

@Repository
public interface CommentRepo extends BaseRepository<Comment, Long>{
    
}
