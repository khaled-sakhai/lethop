package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.security.Token.JwtService;
import com.example.springsocial.security.Token.Token;
import com.example.springsocial.security.Token.TokenResponse;
import com.example.springsocial.security.Token.TokenService;
import com.google.cloud.storage.Option;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final JwtService jwtService;

  private final AuthenticationManager authenticationManager;

  private final TokenService tokenService;

  private final UtilService utilService;



  public TokenResponse authenticateAndGetToken(String email, String password,String userAgent) {

    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(email, password)
    );
      SecurityContextHolder.getContext().setAuthentication(authentication);

    if (authentication.isAuthenticated()) {
      SecurityContextHolder.getContext().getAuthentication().getDetails();
      Token token = utilService.updateTokenInDB(email,null,userAgent);
      return new TokenResponse(token.getAccessToken(), token.getRefreshToken());
    } else {
      throw new UsernameNotFoundException("invalid user request !");
    }
  }


  public TokenResponse refreshToken(String authHeader,String userAgent) throws IOException {
    String refreshToken;
    String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return null;
    }
    refreshToken = authHeader.substring(7);

    Token tokenDB = tokenService
            .findByRefreshToken(refreshToken).orElseThrow();
    User dbUser=tokenDB.getUser();
    userEmail = jwtService.extractUsername(refreshToken);

    TokenResponse tokenResponse = new TokenResponse();

    if (userEmail != null && !tokenDB.isLoggedOut()) {
      if (jwtService.isTokenValid(refreshToken, dbUser.getEmail())) {
        Token token = utilService.updateTokenInDB(dbUser.getEmail(), refreshToken,userAgent);
        tokenResponse.setAccessToken(token.getAccessToken());
        tokenResponse.setRefreshToken(refreshToken);
      } else {
        throw new IOException("INVALID REFRESH TOKEN");
      }
    }

    return tokenResponse;
  }





}


