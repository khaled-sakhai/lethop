package com.example.springsocial.security.Token;

import com.example.springsocial.security.CustomUserDetailsService;
import com.example.springsocial.service.UserService;

import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

@Autowired
private JwtService jwtService;

@Autowired
private CustomUserDetailsService userDetailsServiceImp;

@Autowired
private TokenService tokenService;

@Autowired
private CustomUserDetailsService customUserDetailsService;

private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
        String jwt = getJwtFromRequest(request);
        if (jwt==null) {
            System.out.println("xxxxxx");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractUsername(jwt);
        Optional<Token> tokenFromDB = tokenService.findByToken(jwt);

        if (StringUtils.hasText(jwt) && jwtService.isTokenValid(jwt,email) && tokenFromDB.get().getAccessToken().equals(jwt)) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    } catch (Exception ex) {
        logger.error("Could not set user authentication in security context", ex);
    }

    filterChain.doFilter(request, response);
}

private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
        return bearerToken.substring(7, bearerToken.length());
    }
    return null;
}
}
