package com.example.springsocial.repository;

import org.springframework.stereotype.Repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.Image;

@Repository
public interface ImageRepo extends BaseRepository<Image, Long>{
    
    Image findByFileName(String fileName);

    //void deletByFileName(String fileName);



}
