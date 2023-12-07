package com.example.springsocial.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springsocial.entity.userRelated.UserVerificationCode;
import com.example.springsocial.repository.UserVerificationCodeRepo;

@Service
public class UserVerificationCodeService {
    
    @Autowired
    private UserVerificationCodeRepo userVerificationCodeRepo;


    public UserVerificationCode save(UserVerificationCode userVerificationCode){
        return userVerificationCodeRepo.save(userVerificationCode);
    }

    public UserVerificationCode findByConfirmationCode(String verificationCode){
        return userVerificationCodeRepo.getByConfirmationCode(verificationCode);
    }

    public void removeAfterVerify(UserVerificationCode userVerificationCode){
         userVerificationCodeRepo.delete(userVerificationCode);
    }
}
