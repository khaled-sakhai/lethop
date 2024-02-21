package com.example.springsocial.entity.postRelated;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.util.Constants;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.ArrayUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@Getter
@Setter
public class Tag extends BaseEntity<Long>{
    @Field
    private String tagName;

    public Tag(String tag){
       if( ArrayUtils.contains(Constants.AllowedTags, tag)){
        this.tagName=tag;
       }
       else{
        this.tagName="motivation";
       }
    }

   
}
