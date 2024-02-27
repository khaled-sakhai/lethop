package com.example.springsocial.service;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UtilService {
    @Autowired
    private UserRepo userRepo;

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
