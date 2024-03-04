package com.example.springsocial.controller.auth;

import com.example.springsocial.dto.LoginDto;
import com.example.springsocial.dto.RegisterDto;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.exception.BadRequestException;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.entity.userRelated.UserVerificationCode;
import com.example.springsocial.security.Token.TokenResponse;
import com.example.springsocial.service.AuthService;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.UserVerificationCodeService;
import com.example.springsocial.service.emailService.EmailSenderService;
import com.example.springsocial.util.ProjectUtil;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
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
        confirmationCodeSend(user);
        return ResponseEntity.ok()
        .body( "User registered successfully@ please login using the login information , pleaase verify your email, an email was send to: "+ user.getEmail());
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> isEmailTaken(@RequestBody RegisterDto registerDto){
        boolean result = userService.isEmailTaken(registerDto.getEmail());
        if (result) {
            return ResponseEntity.ok()
        .body( "Valid email, Please continue the registration");
        }
        return ResponseEntity.badRequest()
        .body( "You can't use this email, it's already registered for another user");
    }

    @GetMapping(path = "/confirm-account")
    public ResponseEntity<String> userEmailVerification(@RequestParam("verify") String verificationCode) throws Exception{
      UserVerificationCode userVerificationCode = userVerificationCodeService.findByConfirmationCode(verificationCode);
      if (userVerificationCode !=null) {
        Optional<User> userOptional = userService.findByEmail(userVerificationCode.getUser().getEmail());
        if(!userOptional.isPresent()){
          throw new Exception("User doesn't exist, please verify your verification code and try again!");
        }
        User user = userOptional.get();
        user.setActive(true);
        userService.updateUser(user);

        //we remove the verification code once the user used it,just to clear space
        userVerificationCodeService.removeAfterVerify(userVerificationCode);
        return ResponseEntity.ok().body("Congratulations! your account is fully activated");
      }
      return ResponseEntity.badRequest().body("verification code is not valid");
    }



  @PostMapping("/api/v1/auth/refresh")
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

  //helper
  private void confirmationCodeSend(User user){
     UserVerificationCode userVerificationCode = new UserVerificationCode(user);
     userVerificationCodeService.save(userVerificationCode);

     SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom("mohdz2024@hotmail.com");
        mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:8080/confirm-account?verify="+userVerificationCode.getConfirmationCode());


        emailSenderService.sendEmail(mailMessage);
  }
}
