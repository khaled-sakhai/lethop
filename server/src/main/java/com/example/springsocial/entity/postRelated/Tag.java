package com.example.springsocial.entity.postRelated;

import com.example.springsocial.base.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.ArrayUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@Getter
@Setter
public class Tag extends BaseEntity<Long>{
    private String tagName;

    public Tag(String tag){
       if( ArrayUtils.contains(AllowedTags, tag)){
        this.tagName=tag;
       }
       else{
        this.tagName="motivation";
       }
    }

    private static String[] AllowedTags = {"motivation","addiction","success",
                                             "education","finance","sport",
                                             "health","religion"};
}
