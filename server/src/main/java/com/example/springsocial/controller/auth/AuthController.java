package com.example.springsocial.controller.auth;

import com.example.springsocial.dto.user.EmailRequest;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.enums.VerificationType;
import com.example.springsocial.exception.BadRequestException;
import com.example.springsocial.dto.user.LoginDto;
import com.example.springsocial.dto.user.RegisterDto;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.security.Token.TokenResponse;
import com.example.springsocial.service.AuthService;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.UserVerificationCodeService;
import com.example.springsocial.service.emailService.EmailSenderService;
import com.example.springsocial.util.PathConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private ProfileService profileService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserVerificationCodeService userVerificationCodeService;

    @Autowired
    private EmailSenderService emailSenderService;



    @PostMapping(PathConstants.LOGIN_END_POINT)
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid  LoginDto loginDto,HttpServletRequest request) {
      String userAgent = request.getHeader("User-Agent");

        TokenResponse tokenResponse= authService.authenticateAndGetToken(
            loginDto.getEmail(),
            loginDto.getPassword(),userAgent
          );
        return ResponseEntity.ok(tokenResponse);
    }


    @PostMapping(PathConstants.SIGN_UP)
    public ResponseEntity<?> registerUser( @RequestBody @Valid RegisterDto registerDto) {
        if(userService.isEmailTaken(registerDto.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }
        // Creating user's account
        User user = new User();
        
        user.setEmail(registerDto.getEmail().toLowerCase());
        user.setPassword(registerDto.getPassword());
        user.setProvider(AuthProvider.local);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //new profile to user
        Profile profile = new Profile();
        profile.setUser(user);
        user.setUserProfile(profile);
        profileService.createNewProfile(profile);
        userService.addUser(user);
        // set confirmation code
        userService.confirmationCodeSend(user, VerificationType.SIGNUP);
        return ResponseEntity.ok()
        .body( "User registered successfully!, pleaase verify your email, an email was send to: "+ user.getEmail());
    }

    @PostMapping(PathConstants.EMAIL+"check")
    public ResponseEntity<?> isEmailTaken(@RequestBody @Valid EmailRequest emailRequest){
        boolean result = userService.isEmailTaken(emailRequest.getEmail());
        if (result) {
            return ResponseEntity.badRequest()
                    .body( "You can't use this email, it's already registered for another user");
        }
        return ResponseEntity.ok()
                .body( "Valid email, Please continue the registration");
    }


    @GetMapping(path = PathConstants.EMAIL+"confirm")
    public ResponseEntity<String> userEmailVerification(@RequestParam("verify") String verificationCode) throws Exception{
      if(userService.userEmailVerification(verificationCode)){

        return ResponseEntity.ok().body("Congratulations! your account is fully activated");
      }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("verification code is not valid, please try again or contact us");
    }



  @PostMapping(PathConstants.REFRESH_TOKEN)
  public TokenResponse refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    String userAgent = request.getHeader("User-Agent");
    return authService.refreshToken(
      //his method only requires refresh token in the header, access token not required
      request.getHeader(HttpHeaders.AUTHORIZATION),userAgent
    );
  }



}
