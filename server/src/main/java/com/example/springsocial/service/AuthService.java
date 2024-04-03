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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UserService userService;


  public TokenResponse authenticateAndGetToken(String email, String password,String userAgent) {

    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(email, password)
    );
      SecurityContextHolder.getContext().setAuthentication(authentication);

    if (authentication.isAuthenticated()) {
      SecurityContextHolder.getContext().getAuthentication().getDetails();
      Token token = updateTokenInDB(email,null,userAgent);
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
        Token token = updateTokenInDB(dbUser.getEmail(), refreshToken,userAgent);
        tokenResponse.setAccessToken(token.getAccessToken());
        tokenResponse.setRefreshToken(refreshToken);
      } else {
        throw new IOException("INVALID REFRESH TOKEN");
      }
    }

    return tokenResponse;
  }
  @Transactional
  public Token updateTokenInDB(
    String email,
    String refreshToken,String userAgent
  ) {
    User user = userService.findByEmail(email).orElseThrow();
    Token token = new Token();
    if(refreshToken!=null){
      token.setRefreshToken(refreshToken);
    }else {
      revokeAllTokensByEmail(user);
      token.setRefreshToken(jwtService.generateRefreshToken(user.getEmail()));
    }
    token.setUserAgent(userAgent);
    token.setLoggedOut(false);

    token.setAccessToken(jwtService.generateToken(user.getEmail()));
    tokenService.addToken(token);
    token.setUser(user);
    user.addToken(token);
    userService.updateUser(user);
    return token;
  }

  public void revokeAllTokensByEmail(User user){
    List<Token> userTokens=user.getTokens();
    for(Token token:userTokens){
      token.setLoggedOut(true);
    }
  }


}


