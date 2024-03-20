package com.example.springsocial.validator.permessions;

import java.security.Principal;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.VerificationType;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.UserService;

@Aspect
@Component
public class ValidateUserAspect {
    

    @Autowired
    private UserService userService;

    @Around("@annotation(ValidUser)")
    public Object checkUserActive(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        
        String email=getEmailFromArgument(args[0]);
        Optional<User> user = userService.findByEmail(email);

        if(!user.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("User with the email " + email + "is not registred, please try again with a new email.");
        }
        if (!user.get().isActive()) {
            userService.confirmationCodeSend(user.get(), VerificationType.SIGNUP);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User account not fully activated. We have sent the activation link to " + email + ". Please verify your email for the activation link and try again.");
        }
        return joinPoint.proceed();
        
    }


    private String getEmailFromArgument(Object argument) throws Exception{
        
        if(argument instanceof String){
           return (String) argument;
             }
             else if(argument instanceof Principal){
                 Principal principal = (Principal) argument;
                 return principal.getName();
             } 
             else if(argument instanceof UserPrincipal){
                 UserPrincipal principal = (UserPrincipal) argument;
                 return principal.getEmail();
             } 
             else {
                 throw new Exception("Invalid request, please try again");
             }

    }
}
