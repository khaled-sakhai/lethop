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

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
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

  private boolean isArchived=false;
  
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "post_tag",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
    private Set<Tag> listTags;

  @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, 
                       CascadeType.DETACH,  CascadeType.REFRESH},fetch = FetchType.LAZY)
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
  private Set<User> savedByUsers = new HashSet<>();

  @JsonIgnore
  @ManyToMany(mappedBy = "likedPosts",fetch = FetchType.LAZY , cascade = CascadeType.ALL)
  private Set<User> likedByUsers = new HashSet<>();

  @Column(name = "saved_posts_count")
  private int savesCount;


  @Column(name = "liked_posts_count")
  private int likesCount;

  private boolean hasComments;

  /// comments
  @JsonIgnore
  @OneToMany(mappedBy = "post")
  private Set<Comment> postComments =new HashSet<>();

  public int getNumberOfComments(){
    int count=0;
    for(Comment comment: this.postComments){
      if(comment.getNumberOfReplies()>0){
        count =+comment.getNumberOfReplies();
      }
      count++;
    }
    return count;
  }

  // ... getters and setters


  public void updateSavedCount() {
    this.savesCount= savedByUsers.size();
  }

  public void  updateLikedCount() {
    this.likesCount= likedByUsers.size();
 }



}
