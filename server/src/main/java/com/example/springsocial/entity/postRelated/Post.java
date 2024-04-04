package com.example.springsocial.entity.postRelated;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.Image;
import com.example.springsocial.entity.userRelated.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Indexed
public class Post extends BaseEntity<Long> {
  @Field
  private String content;
  @Field
  private String title;

  @ManyToMany(fetch = FetchType.LAZY,cascade={ CascadeType.MERGE, CascadeType.PERSIST })
  @JoinTable(
    name = "post_tag",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  @IndexedEmbedded
    private Set<Tag> listTags= new HashSet<>();

  @ManyToOne(cascade={ CascadeType.MERGE, CascadeType.PERSIST },fetch = FetchType.LAZY)
  @JoinColumn(name="category_id", referencedColumnName = "id")
  @IndexedEmbedded
  private Category category;


  @ManyToOne(fetch = FetchType.LAZY,cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonIgnore
  private User user;

  @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
  @JsonIgnore
  private List<Image> postImages=new ArrayList<>();

  @JsonIgnore
  private boolean isAnonymous= Boolean.FALSE;

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

  @Column(name = "comments_posts_count")
  private int commentsCount=0;

  
  /// comments
  @JsonIgnore
  @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE, orphanRemoval = true)
  private Set<Comment> postComments =new HashSet<>();
  // ... getters and setters

  public void removeTags(){
    listTags.clear();
  }



  public void updateCommentCount() {
    this.commentsCount= postComments.size();
  }
  public void updateSavedCount() {
    this.savesCount= savedByUsers.size();
  }
  public void  updateLikedCount() {
    this.likesCount= likedByUsers.size();
 }

 public void removeCommentFromPost(Comment comment){
  this.postComments.remove(comment);
 }


}
