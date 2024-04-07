package com.example.springsocial.entity.postRelated;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.userRelated.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "replies")
@Table(name = "replies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE replies SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Reply  extends BaseEntity<Long>{

    
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Author of the reply
}
