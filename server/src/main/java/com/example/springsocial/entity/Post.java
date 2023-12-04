package com.example.springsocial.entity;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.Tag;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import javax.persistence.FetchType;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post extends BaseEntity<Long> {
  private String content;

  private String title;

  @Enumerated(EnumType.STRING)
  private Tag tag;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "post_image",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "image_id")
  )
  private List<Image> listImages=new ArrayList<>();

  private boolean isActive;


   public void setTagPost(String postTag){
    if(Tag.isInEnum(postTag)){
      tag = Tag.valueOf(postTag);
  }
  else{
    tag=Tag.HOPE;
  }
}

}
