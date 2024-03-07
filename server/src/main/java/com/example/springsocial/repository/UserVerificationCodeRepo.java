package com.example.springsocial.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.userRelated.UserVerificationCode;

@Repository
public interface UserVerificationCodeRepo extends BaseRepository<UserVerificationCode,Long> {

    Optional<UserVerificationCode> findByConfirmationCode(String confirmationCode);
    
}
