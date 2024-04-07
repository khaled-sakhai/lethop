package com.example.springsocial.repository;

import com.example.springsocial.entity.postRelated.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.postRelated.Reply;

@Repository
public interface ReplyRepo  extends BaseRepository<Reply, Long>, JpaSpecificationExecutor<Reply> {

    Page<Reply> findByComment(Comment comment, Pageable paging);
}
