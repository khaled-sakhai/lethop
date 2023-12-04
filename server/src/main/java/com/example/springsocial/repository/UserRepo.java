package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.userRelated.User;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends BaseRepository<User, Long> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);
}
