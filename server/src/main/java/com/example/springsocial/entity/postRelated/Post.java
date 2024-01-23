package com.example.springsocial.entity.postRelated;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.User;

import javax.persistence.CascadeType;

import javax.persistence.Entity;

import javax.persistence.Table;
import javax.persistence.FetchType;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "post_tag",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
    private Set<Tag> listTags;


  @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, 
                       CascadeType.DETACH,  CascadeType.REFRESH})
  @JoinColumn(name="category_id", referencedColumnName = "id")
  private Category category;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @OneToOne(cascade=CascadeType.ALL)
  @JoinColumn(name = "profile_image_id", referencedColumnName = "id")
  private Image postImage;

  
  private boolean isActive;


  @ManyToMany(mappedBy = "savedPosts")
  private Set<User> savedByUsers;

  // ... getters and setters

  public int getSavedCount() {
      return savedByUsers.size();
  }



}
