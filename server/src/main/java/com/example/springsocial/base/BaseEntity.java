package com.example.springsocial.base;


import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Data
@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class })
public abstract class BaseEntity<ID> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  
  private Long id;

  @CreatedBy
  private String createdBy;

  @CreatedDate
  private Date createdDate;

  @LastModifiedBy
  private String lastModifiedBy;

  @LastModifiedDate
  private Date lastModifiedDate;
}
