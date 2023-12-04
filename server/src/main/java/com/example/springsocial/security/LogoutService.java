package com.example.springsocial.security;

import com.example.springsocial.security.Token.Token;
import com.example.springsocial.security.Token.TokenRepo;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

  @Autowired
  private TokenRepo tokenRepo;

  @Override
  public void logout(
    HttpServletRequest request,
    HttpServletResponse response,
    Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");
    final String jwt;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    jwt = authHeader.substring(7);

    Optional<Token> token = tokenRepo.findByAccessToken(jwt);
    if (token.isPresent()) {
      tokenRepo.delete(token.get());
      SecurityContextHolder.clearContext();
    }
  }
}
