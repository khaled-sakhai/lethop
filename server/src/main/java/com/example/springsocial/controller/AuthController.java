package com.example.springsocial.controller;

import com.example.springsocial.dto.LoginDto;
import com.example.springsocial.dto.RegisterDto;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.exception.BadRequestException;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.security.Token.TokenResponse;
import com.example.springsocial.service.AuthService;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.util.ProjectUtil;
import org.springframework.http.HttpHeaders;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private ProfileService profileService;

    @Autowired
    private AuthService authService;



    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDto loginDto,HttpServletRequest request) {
      String userAgent = request.getHeader("User-Agent");

        TokenResponse tokenResponse= authService.authenticateAndGetToken(
            loginDto.getEmail(),
            loginDto.getPassword(),userAgent
          );
        return ResponseEntity.ok(tokenResponse);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        if(userService.isEmailTaken(registerDto.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }
        // Creating user's account
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Profile profile = new Profile();
        profile.setUser(user);
        profileService.createNewProfile(profile);
        user.setUserProfile(profile);
        userService.updateUser(user);

        return ResponseEntity.ok()
        .body( "User registered successfully@ please login using the login information");
    }



    @PostMapping("/checkemail")
    public ResponseEntity<?> isEmailTaken(@RequestBody RegisterDto registerDto){
        boolean result = userService.isEmailTaken(registerDto.getEmail());
        if (result) {
            return ResponseEntity.ok()
        .body( "Valid email, Please continue the registration");
        }
        return ResponseEntity.badRequest()
        .body( "You can't use this email, it's already registred for another user");
    }



  @PostMapping("/api/v1/auth/refresh")
  public TokenResponse refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    String userAgent = request.getHeader("User-Agent");
    return authService.refreshToken(
      //now this only requires refresh token in the header, access token not required
      request.getHeader(HttpHeaders.AUTHORIZATION),userAgent
    );
  }

}
