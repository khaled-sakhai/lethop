package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepo extends BaseRepository<User, Long> , JpaSpecificationExecutor<User> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);


  @Query(nativeQuery = true, value = "SELECT * FROM users WHERE deleted = true")
  Page<User> findAllDeleted(Pageable pageable);

  @Query(nativeQuery = true, value = "SELECT * FROM users WHERE id = :postId")
  Optional<User> findAnyUserById(@Param("postId") Long postId);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM users WHERE id = :userId", nativeQuery = true)
  void deleteUserById(@Param("userId") Long userId);
}
