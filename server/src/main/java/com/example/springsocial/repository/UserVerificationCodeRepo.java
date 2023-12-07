package com.example.springsocial.repository;

import org.springframework.stereotype.Repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.userRelated.UserVerificationCode;

@Repository
public interface UserVerificationCodeRepo extends BaseRepository<UserVerificationCode,Long> {

    UserVerificationCode getByConfirmationCode(String confirmationCode);
    
}
