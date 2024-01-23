package com.example.springsocial.entity.postRelated;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ArrayUtils;

import com.example.springsocial.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category extends BaseEntity<Long>{
  @NotNull
  private String category;

  @OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
   @JoinTable(
    name = "post_category",
    joinColumns = @JoinColumn(name = "category_id"),
    inverseJoinColumns = @JoinColumn(name = "post_id")
  )
  private List<Post> posts = new ArrayList<>();

   public Category(String category){
       if( ArrayUtils.contains(AllowedCategory, category)){
        this.category=category;
       }
       else{
        this.category="question";
       }
    }

    private static String[] AllowedCategory = {"Good","learn","question"};

}