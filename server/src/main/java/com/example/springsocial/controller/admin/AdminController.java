package com.example.springsocial.controller.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.service.UserService;
import java.util.Arrays;

@RestController
public class AdminController {
    
    @Autowired
    private UserService userService;


   @GetMapping(value = "/api/admin/users")    
   public List<User> getAllUsers(Principal principal){
        return userService.findAll();
   }

    @GetMapping(value = "/api/admin/users/active/{provider}")    
    public List<User> getActiveByProvider(@PathVariable AuthProvider provider){
        return userService.findAllActiveUsers(provider);
    }

    @GetMapping(value = "/api/admin/users/unactive/{provider}")    
    public List<User> getNonActiveByProvider(@PathVariable AuthProvider provider){
        return userService.findAllNonActiveUsers(provider);
    }
    
}

