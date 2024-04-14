package com.example.springsocial.service.postService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.example.springsocial.util.Constants;
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
          return tagFromDB.orElseGet(() -> tagRepo.save(tag));
    }

    public Set<Tag> setTagsToPost(String tagsListAsString){
        Set<Tag> tags = new HashSet<>();
        if(tagsListAsString!=null && !tagsListAsString.isBlank() && tagsListAsString.trim().length()>9) {
            String []  items = tagsListAsString.split("\\s*,\\s*");
            for(String tag: items){
                Tag tagDb = this.saveTag(new Tag(tag));
                tags.add(tagDb);
            }
        }
        else tags.add(this.saveTag(new Tag(Constants.AllowedTags[0])));
        return tags;
    }
}
