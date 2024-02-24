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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "post_tag",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  @IndexedEmbedded
    private Set<Tag> listTags;

  @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, 
                       CascadeType.DETACH,  CascadeType.REFRESH},fetch = FetchType.LAZY)
  @JoinColumn(name="category_id", referencedColumnName = "id")
   @JsonIgnore
  @IndexedEmbedded
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

  @Column(name = "comments_posts_count")
  private int commentsCount=0;

  
  /// comments
  @JsonIgnore
  @OneToMany(mappedBy = "post")
  private Set<Comment> postComments =new HashSet<>();
  // ... getters and setters


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
