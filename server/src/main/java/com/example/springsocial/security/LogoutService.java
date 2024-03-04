package com.example.springsocial.security;

import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.security.Token.Token;
import com.example.springsocial.security.Token.TokenRepo;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

  @Autowired
  private TokenRepo tokenRepo;

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private ClientRegistrationRepository clientRegistrationRepository;

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

    try {
      Optional<Token> token = tokenRepo.findByAccessToken(jwt);

      if (token.isPresent()) {
        token.get().setLoggedOut(true);
        tokenRepo.save(token.get());
      }
    } catch (NoSuchElementException e) {
      // Handle missing token (log or return appropriate response)
    }
  }
}
