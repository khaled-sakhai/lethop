package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends BaseRepository<User, Long> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
  List<User> findByIsActiveTrue();
  List<User> findByIsActiveTrueAndProvider(AuthProvider provider);

  List<User> findByIsActiveFalseAndProvider(AuthProvider provider);
  

}
