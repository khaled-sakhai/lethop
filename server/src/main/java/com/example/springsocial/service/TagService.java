package com.example.springsocial.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.postRelated.Category;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.repository.TagRepo;
@Service
public class TagService {
    
    @Autowired
    private TagRepo tagRepo;


      public Tag saveTag(Tag tag){
        Optional<Tag> tagFromDB = tagRepo.findByTagName(tag.getTagName());
        if(!tagFromDB.isPresent()){
            return tagRepo.save(tag);
        }
        else{
            return tagFromDB.get();
        }
    }

}
