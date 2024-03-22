package com.example.springsocial.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.Features.UserVerificationCode;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.VerificationType;

@Repository
public interface UserVerificationCodeRepo extends BaseRepository<UserVerificationCode,Long> {

    Optional<UserVerificationCode> findByConfirmationCode(String confirmationCode);
    Optional<UserVerificationCode> findByUserAndType(User user,VerificationType verificationType);

    
}
