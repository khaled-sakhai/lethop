package com.example.springsocial.repository;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.postRelated.Comment;

@Repository
public interface CommentRepo extends BaseRepository<Comment, Long>{

    Page<Comment> findByPost(Post post, Pageable paging);

    Page<Comment> findByUser(User user, Pageable paging);
}
