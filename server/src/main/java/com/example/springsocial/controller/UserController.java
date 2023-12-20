package com.example.springsocial.controller;

import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.repository.RoleRepo;
import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.security.Token.TokenRepo;
import com.example.springsocial.service.ProfileService;
import com.example.springsocial.service.UserService;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private RoleRepo roleRepo;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }


    @DeleteMapping("/user/")
    public ResponseEntity<String> removeUser(Principal principal){
        Optional<User> user = userService.findByEmail(principal.getName());
        if (user.isPresent()) {
            System.out.println(principal.getName() + " --- id -- "+principal.getName());
            
            user.get().removeAllRoles();   
            userService.deleteUser(user.get());

            return ResponseEntity.ok().body("user removed successfully");
        }

        return ResponseEntity.badRequest().body("user removing failed");
    }

    
}
