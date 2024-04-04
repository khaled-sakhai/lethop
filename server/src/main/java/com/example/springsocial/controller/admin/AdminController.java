package com.example.springsocial.controller.admin;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.springsocial.dto.post.PostDto;
import com.example.springsocial.dto.user.UserDto;
import com.example.springsocial.service.admin.UserAdmin;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import com.example.springsocial.repository.ImageRepo;

import com.example.springsocial.service.UserService;
import com.example.springsocial.service.postService.CategoryService;
import com.example.springsocial.service.postService.PostService;
import com.example.springsocial.service.postService.TagService;

import java.util.Arrays;

@RestController
@AllArgsConstructor
public class AdminController {

    private final UserAdmin userAdmin;
    private final UserService userService;



   @GetMapping(value = "/api/admin/users")    
   public ResponseEntity<Page<User>>  getAllUsers(@RequestParam(required = false) Boolean isActive,
                                 @RequestParam(required = false) String provider,
                                 @RequestParam(required = false) Long userId,
                                 @RequestParam(required = false) Boolean needProfileUpdate,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                 @RequestParam(defaultValue = "20") int size,
                                 @RequestParam(defaultValue = "desc") String sortDirection){
       Page<User> usersPage = userAdmin.findAll(isActive,provider,needProfileUpdate,userId ,page, size, sortBy, sortDirection);
       //Page<UserDto> postDtos= usersPage.map(UserDto::new);
       if (usersPage.isEmpty()) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       return new ResponseEntity<>(usersPage, HttpStatus.OK);
   }


   @DeleteMapping(path = "api/admin/user/{userId}")
   @Transactional
   public String removeUser(@PathVariable long userId,@RequestParam boolean finalDelete){
       Optional<User> user = userAdmin.findAnyUserById(userId);
       if(user.isPresent()){
           if(finalDelete){
               userAdmin.finalDeleteById(user.get());
           }
           else{
               userService.deleteUser(user.get());
           }
           return "Removed!";
       }
       else return "Not Removed";
   }



}

