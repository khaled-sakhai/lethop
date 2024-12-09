package com.example.springsocial.controller.user;

import com.example.springsocial.dto.user.UserDto;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.dto.user.PasswordRequest;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.VerificationType;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.security.Token.TokenRepo;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.util.PathConstants;
import com.example.springsocial.validator.permessions.ValidUser;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.Email;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {


    private final UserService userService;
    

    @GetMapping(PathConstants.API_V1+"user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        Optional<User> userOptional = userService.findByEmail(userPrincipal.getEmail());
        if (userOptional.isPresent()){

            return ResponseEntity.ok().body(new UserDto(userOptional.get()));
        }
        else return ResponseEntity.badRequest().body(null);

    }

    @DeleteMapping(PathConstants.API_V1+"/user")
    public ResponseEntity<String> removeUser(Principal principal){
        Optional<User> user = userService.findByEmail(principal.getName());
        if (user.isPresent()) {
            userService.deleteUser(user.get());
            return ResponseEntity.ok().body("user removed successfully");
        }
        return ResponseEntity.badRequest().body("user removing failed");
    }

    @PostMapping(PathConstants.API_V1+"user/password")
    public ResponseEntity<String> updatePassword(Principal principal,
                                                  @RequestParam("password") String newPassword, 
                                                  @RequestParam("oldpassword") String oldPassword){

      Optional<User> user = userService.findByEmail(principal.getName());
           if (user.isPresent()) {
            if(!user.get().getProvider().toString().equalsIgnoreCase("local")){
                throw new IllegalArgumentException("not a local user");
            }
             if(userService.checkIfValidOldPassword(oldPassword, user.get())){
                throw new IllegalArgumentException("password doesnt match the original one");
            }
            userService.changePassword(newPassword, user.get());
            return ResponseEntity.ok().body("change password!");
           }
            return ResponseEntity.badRequest().body("password change has failed!");
    }

    @PostMapping(PathConstants.API_V1+"user/email/update")
    public ResponseEntity<String> updateUserEmail(Principal principal,@RequestBody Map<String, String> requestBody){
        String email = requestBody.get("email");
        User user = userService.findByEmail(email).orElseThrow();
        userService.updateEmail(user, email);
        return ResponseEntity.ok().body("email has been updated");
    }

    // send request / generate code,email user
    @GetMapping(PathConstants.API_V1+PathConstants.API_PUBLIC+"password/reset")
    @ValidUser()
    public ResponseEntity<String> resetPassword(@RequestParam @Email String email){
        User user = userService.findByEmail(email).orElseThrow();

        userService.confirmationCodeSend(user, VerificationType.PASSWORD);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("We've sent you an email to update your password");
    }

    /// user click on the link { activate the verify code}
    @GetMapping(PathConstants.API_V1+PathConstants.API_PUBLIC+"password/verify")
    public ResponseEntity<String> resetPasswordVerification(@RequestParam("code") String verificationCode){
        try {
           userService.validateVerificationCode(verificationCode);
           return ResponseEntity.status(HttpStatus.ACCEPTED).body("You're able to change your password now..");
        } 
        catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification code, please try again");
        }
    }

    @PostMapping(path = PathConstants.API_V1+PathConstants.API_PUBLIC+"password/reset")
    public ResponseEntity<String> userPasswordResetVerification(@RequestBody PasswordRequest passwordRequest){
      try{
        userService.userPasswordResetVerification(passwordRequest.getVerificationCode(),passwordRequest.getPassword());
        return ResponseEntity.ok().body("Password has been updated successfully");
      }
      catch(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("verification code is not valid, please try again or contact us");
      } 
    }

     
}
