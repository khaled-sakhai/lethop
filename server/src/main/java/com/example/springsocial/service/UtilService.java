package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.UserRepo;
import com.example.springsocial.security.Token.JwtService;
import com.example.springsocial.security.Token.Token;
import com.example.springsocial.security.Token.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UtilService {
    private final UserRepo userRepo;
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtService jwtService;

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

    public void revokeAllTokensByEmail(User user) {
        List<Token> userTokens = user.getTokens();
        for (Token token : userTokens) {
            token.setLoggedOut(true);
        }
    }

    public User getUserFromPrincipal(Principal principal){
        Optional<User> user = userRepo.findByEmail(principal.getName());
        return user.orElseThrow();
    }
    public Profile getProfileFromPrincipal(Principal principal){
        Optional<User> user = userRepo.findByEmail(principal.getName());
        if (user.isPresent()){
            return user.get().getUserProfile();
        }
        else throw new NoSuchElementException("invalid user");
    }
}
