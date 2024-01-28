package com.example.springsocial.entity.postRelated;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
   @JsonIgnore
  private Category category;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonIgnore
  private User user;

  @OneToOne(cascade=CascadeType.ALL)
  @JoinColumn(name = "post_image_id", referencedColumnName = "id")
  @JsonIgnore
  private Image postImage;

  @JsonIgnore
  private boolean isPublic=true;
  @JsonIgnore
  private boolean isAnonymous= false;

  @JsonIgnore
  @ManyToMany(mappedBy = "savedPosts",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
  private Set<User> savedByUsers;

  public void removePostFromSavedLists(Post post) throws Exception{
    for(User user:this.savedByUsers){
      user.unsavePost(post);
    }
    this.savedByUsers.clear();
  }

  public void removePostfromUserSavedList(Post post,User user){
    this.savedByUsers.remove(user);
  }
  // ... getters and setters


  public int getSavedCount() {
    if(savedByUsers!=null){
       return savedByUsers.size();
    }
     return 0;
  }



}
