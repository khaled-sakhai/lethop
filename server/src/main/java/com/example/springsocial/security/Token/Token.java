package com.example.springsocial.security.Token;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.userRelated.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Token extends BaseEntity<Long> {

  private String refreshToken;

  private String accessToken;

  private String userAgent;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;
}
