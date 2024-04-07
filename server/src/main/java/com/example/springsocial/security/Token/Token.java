package com.example.springsocial.security.Token;

import com.example.springsocial.base.BaseEntity;
import com.example.springsocial.entity.userRelated.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity(name = "tokens")
@Table(name = "tokens")

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@SQLDelete(sql = "UPDATE tokens SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Token extends BaseEntity<Long> {

  private String refreshToken;

  private String accessToken;

  private boolean isLoggedOut;

  private String userAgent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonIgnore
  private User user;
}
