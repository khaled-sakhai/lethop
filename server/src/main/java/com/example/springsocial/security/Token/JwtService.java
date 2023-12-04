package com.example.springsocial.security.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  public static final String SECRET =
    "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

  private String secretKey="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
  private final long jwtExpiration=86400000 ;
  private final long refreshExpiration=604800000 ;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(String email) {
    return generateToken(new HashMap<>(), email);
  }

  public String generateToken(
          Map<String, Object> extraClaims,
          String email
  ) {
    return buildToken(extraClaims, email, jwtExpiration);
  }

  public String generateRefreshToken(
          String email
  ) {
    return buildToken(new HashMap<>(), email, refreshExpiration);
  }

  private String buildToken(
          Map<String, Object> extraClaims,
          String email,
          long expiration
  ) {
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(email)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public boolean isTokenValid(String token, String email) {
    final String username = extractUsername(token);
    return (username.equals(email)) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
