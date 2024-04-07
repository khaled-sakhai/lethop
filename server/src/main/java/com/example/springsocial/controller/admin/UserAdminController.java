package com.example.springsocial.controller.admin;

import java.util.List;
import java.util.Optional;

import com.example.springsocial.dto.profile.ProfileDto;
import com.example.springsocial.dto.user.RegisterDto;
import com.example.springsocial.dto.user.UserDto;
import com.example.springsocial.service.admin.UserAdmin;
import com.example.springsocial.validator.validators.ValidPostSortBy;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.springsocial.entity.userRelated.User;

import com.example.springsocial.service.UserService;

@RestController
@AllArgsConstructor
public class UserAdminController {

    private final UserAdmin userAdmin;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;



   @GetMapping(value = "/api/admin/users")
   public ResponseEntity<Page<UserDto>>  getAllUsers(@RequestParam(required = false) Boolean isActive,
                                 @RequestParam(required = false) String provider,
                                 @RequestParam(required = false) Long userId,
                                 @RequestParam(required = false) Boolean needProfileUpdate,
                                 @RequestParam(required = false) String role,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "lastModifiedDate") @ValidPostSortBy String sortBy,
                                 @RequestParam(defaultValue = "20") int size,
                                 @RequestParam(defaultValue = "desc") String sortDirection){
       Page<User> usersPage = userAdmin.findAll(isActive,provider,needProfileUpdate,userId ,role,page, size, sortBy, sortDirection);
       Page<UserDto> userDtos= usersPage.map(UserDto::new);
       if (userDtos.isEmpty()) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       return new ResponseEntity<>(userDtos, HttpStatus.OK);
   }
    @GetMapping(value = "/api/admin/user/id/{userId}")
    public ResponseEntity<User>  getUserById(@PathVariable Long userId){
        Optional<User> userOptional = userAdmin.findAnyUserById(userId);
        if (userOptional.isPresent()){
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping(value = "/api/admin/user/email/{email}")
    public ResponseEntity<User>  getUserByEmail(@PathVariable String email){
        Optional<User> userOptional = userAdmin.findAnyUserByEmail(email);
        if (userOptional.isPresent()){
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

   @DeleteMapping(path = "api/admin/user/{userId}")
   @Transactional
   public ResponseEntity<String> removeUser(@PathVariable long userId,@RequestParam(required = false) boolean finalDelete){
       Optional<User> user = userAdmin.findAnyUserById(userId);
       if(user.isPresent()){
           if(finalDelete){
               userAdmin.finalDeleteById(user.get());
           }
           else{
               userService.deleteUser(user.get());
           }
           return ResponseEntity.ok().body("User removed!");
       }
       else return ResponseEntity.badRequest().body("Not Removed");
   }

    @PostMapping (path = "api/admin/user/new")
    public void addUser(@RequestBody RegisterDto registerDto){
       userAdmin.adminAddNewUser(registerDto);
   }

   @Transactional
   @PutMapping(path = "api/admin/user/{userId}/edit")
   public ResponseEntity<String> editUser(@PathVariable Long userId,
                        @RequestPart RegisterDto registerDto,
                        @RequestParam(required = false) List<String> roles){
       Optional<User> user = userAdmin.findAnyUserById(userId);
       if(user.isPresent()){
           user.get().setEmail(registerDto.getEmail());
           user.get().setPassword(passwordEncoder.encode(registerDto.getPassword()));
           userAdmin.adminEditUserRoles(user.get(),roles);
        return ResponseEntity.ok().body("user updated!");
       } else return ResponseEntity.badRequest().body("User Not updated");
   }

}

