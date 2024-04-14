package com.example.springsocial.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.Features.UserVerificationCode;
import com.example.springsocial.repository.UserVerificationCodeRepo;

@Service
public class UserVerificationCodeService {
    
    @Autowired
    private UserVerificationCodeRepo userVerificationCodeRepo;


    public UserVerificationCode save(UserVerificationCode userVerificationCode){
        return userVerificationCodeRepo.save(userVerificationCode);
    }


    
}
