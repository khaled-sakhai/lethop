package com.example.springsocial.security.Token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
  Optional<Token> findByRefreshToken(String token);

  Optional<Token> findByAccessToken(String accessToken);

  void deleteTokenByUserId(Long userId);

  List<Token> findByUserEmail(String email);



}
