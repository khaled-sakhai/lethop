package com.example.springsocial.security.Token;

import java.util.List;
import java.util.Optional;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.Features.UserVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepo extends BaseRepository<Token,Long> {
  Optional<Token> findByRefreshToken(String token);

  Optional<Token> findByAccessToken(String accessToken);

  void deleteTokenByUserId(Long userId);

  List<Token> findByUserEmail(String email);

  List<Token> findAllByIsLoggedOut(boolean isLoggedOut);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM tokens WHERE id = :tokenId", nativeQuery = true)
  void deleteTokenById(@Param("tokenId") Long tokenId);



}
