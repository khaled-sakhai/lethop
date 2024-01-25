package com.example.springsocial.entity.userRelated;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.security.Token.Token;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL )
  @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();
  

  @OneToOne(cascade = CascadeType.ALL) //optional = false)
  @JoinColumn(name = "profile_id", referencedColumnName = "id")
  private Profile userProfile;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "user",cascade = CascadeType.ALL)
  private Token token;


  //posts and saved posts
  @OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
   @JoinTable(
    name = "user_posts",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "post_id")
  )

  private List<Post> posts = new ArrayList<>();

  @Column(name = "isSavedPostPrivate", columnDefinition = "boolean default true")
  private boolean isSavedPostPrivate=true;

  @ManyToMany
  @JoinTable(name = "saved_posts",
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "post_id"))
  private List<Post> savedPosts=new ArrayList<>();

  @Column(name = "saved_posts_count")
  private int savedPostsCount;

/////helpers
public void addRoles(Role ...role) {
  for(Role myRole:role){
    this.roles.add(myRole);
  }
  }

  public void removeAllRoles(){
    this.roles=new HashSet<>();
  }
  
}
