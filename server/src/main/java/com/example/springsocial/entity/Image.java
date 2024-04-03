package com.example.springsocial.entity;

import com.example.springsocial.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Image extends BaseEntity<Long> {
    private String url;
    private String fileName;
}
