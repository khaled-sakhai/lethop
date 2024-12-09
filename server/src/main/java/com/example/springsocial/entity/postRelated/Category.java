package com.example.springsocial.entity.postRelated;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.ArrayUtils;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.util.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Field;


@Entity(name = "categories")
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Category extends BaseEntity<Long>{
  @NotNull
  @Field
  private String category;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinTable(
    name = "post_category",
    joinColumns = @JoinColumn(name = "category_id"),
    inverseJoinColumns = @JoinColumn(name = "post_id")
  )
 @JsonIgnoreProperties("category")
  @JsonIgnore
  private List<Post> posts = new ArrayList<>();

  public void removePostFromCategoryById(long postId) {
    posts.removeIf(post -> post.getId() == postId);
}

public void removePostFromCategory(Post post){
      posts.remove(post);
}

   public Category(String category){
       if( ArrayUtils.contains(Constants.AllowedCategory, category.toLowerCase())){
        this.category=category.toLowerCase();
       }
       else{
        this.category="question";
       }
    }

    public void addPostToCategory(Post post){
      this.posts.add(post);
    }

}