package com.example.springsocial.repository;

import org.springframework.stereotype.Repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.postRelated.Reply;

@Repository
public interface ReplyRepo  extends BaseRepository<Reply, Long>{

}
