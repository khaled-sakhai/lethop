package com.example.springsocial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.entity.postRelated.Tag;
@Repository

public interface TagRepo   extends JpaRepository<Tag, Long>{
    
    Optional<Tag> findByTagName(String tag);
}