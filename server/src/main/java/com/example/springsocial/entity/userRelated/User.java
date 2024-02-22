package com.example.springsocial.entity.userRelated;

import com.example.springsocial.entity.postRelated.Comment;
import com.example.springsocial.entity.postRelated.Reply;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.security.Token.Token;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//to fix nested recursions problem between 1-1 relationship
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id"
)
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class User extends BaseEntity<Long> {

   // basics
   @NotNull
   @Email(message = "Please provide a valid email address")
   private String email;
   @JsonIgnore
   private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

  @Column(name = "isActive", columnDefinition = "boolean default false")
  private boolean isActive = false;

  @Column(name = "needProfileUpdate", columnDefinition = "boolean default false")
  private boolean needProfileUpdate = true;

  // user relationships
  @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
  @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();
  

  @OneToOne(cascade = CascadeType.ALL) //optional = false)
  @JoinColumn(name = "profile_id", referencedColumnName = "id")
  private Profile userProfile;

  @JsonIgnore
  @OneToOne(fetch = FetchType.LAZY, mappedBy = "user",cascade = CascadeType.REMOVE, orphanRemoval=true)
  private Token token;


  //posts and saved posts
  @OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE,orphanRemoval = true)
   @JoinTable(
    name = "user_posts",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "post_id")
  )

  private List<Post> posts = new ArrayList<>();

  @Column(name = "isSavedPostPrivate", columnDefinition = "boolean default true")
  private boolean isSavedPostPrivate=true;

  @ManyToMany(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
  @JoinTable(name = "saved_posts",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "post_id"))
  private List<Post> savedPosts=new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY ,cascade = CascadeType.REMOVE)
  @JoinTable(name = "liked_posts",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "post_id"))
  private List<Post> likedPosts=new ArrayList<>();


  @Column(name = "saved_posts_count")
  private int savedPostsCount;


  @Column(name = "liked_posts_count")
  private int likedPostsCount;
  @Column(name = "comment_posts_count")
  private int commentPostsCount;
  /// comments
  @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE, orphanRemoval=true)
  private Set<Comment> userComments = new HashSet<>();

  @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE, orphanRemoval=true)
  private Set<Reply> userReplies = new HashSet<>();

/////helpers
public void addRoles(Role ...role) {
  this.roles.addAll(Arrays.asList(role));
  }

  public void removeAllRoles(){
    this.roles=new HashSet<>();
  }

  public void updateSavedCounter(){
    this.savedPostsCount=this.getSavedPosts().size();
  }
  public void updateLikedCounter(){
    this.likedPostsCount=this.getLikedPosts().size();
  }

  public void updateCommentsCounter(){
    this.commentPostsCount = this.userComments.size();
  }
  public void updateRepliesCounter(){
    this.commentPostsCount = this.userReplies.size();
  }
  //// likes

}
