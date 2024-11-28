package com.example.springsocial.security.Token;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TokenService {

  @Autowired
  private TokenRepo tokenRepo;

  public List<Token> getAll() {
    return tokenRepo.findAll();
  }

  public Optional<Token> findByToken(String token) {
    return tokenRepo.findByAccessToken(token);
  }

  public Optional<Token> findByRefreshToken(String refreshtoken) {
    return tokenRepo.findByRefreshToken(refreshtoken);
  }

  public void deletTokenByUserId(Long id) {
    tokenRepo.deleteTokenByUserId(id);
  }

  public void deleteTokenById(Long id) {
    tokenRepo.deleteById(id);
  }

  public Token addToken(Token token) {
    return tokenRepo.save(token);
  }

  public List<Token> findByEmail(String email){
    return tokenRepo.findByUserEmail(email);
    
  }

  public void updateToken(Token token){
    tokenRepo.save(token);
  }
  public void updateAllTokens(List<Token> tokens){
    tokenRepo.saveAll(tokens);
  }

  public void deletToken(Token token) {
    tokenRepo.delete(token);
  }
}
