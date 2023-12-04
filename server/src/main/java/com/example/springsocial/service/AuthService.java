package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.security.Token.JwtService;
import com.example.springsocial.security.Token.Token;
import com.example.springsocial.security.Token.TokenResponse;
import com.example.springsocial.security.Token.TokenService;
import java.io.IOException;
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
    boolean isRefreshTokenExist = tokenService
      .findByRefreshToken(refreshToken)
      .isPresent();
    userEmail = jwtService.extractUsername(refreshToken);
    TokenResponse tokenResponse = new TokenResponse();
    if (userEmail != null && isRefreshTokenExist) {
      if (jwtService.isTokenValid(refreshToken, userEmail)) {
        Token token = updateTokenInDB(userEmail, refreshToken,userAgent);
        tokenResponse.setAccessToken(token.getAccessToken());
        tokenResponse.setRefreshToken(refreshToken);
      } else {
        throw new IOException("WRONG REFRESH TOKEN");
      }
    }

    return tokenResponse;
  }

  private Token updateTokenInDB(
    String email,
    String refreshToken,String userAgent
  ) {
    User user = userService.findByEmail(email).orElseThrow();
     tokenService.deletTokenByUserId(user.getId());
     
    Token token = new Token();
    token.setRefreshToken(jwtService.generateRefreshToken(user.getEmail()));
    token.setAccessToken(jwtService.generateToken(user.getEmail()));
    token.setUser(user);
    tokenService.addToken(token,userAgent);
    return token;
  }
}
