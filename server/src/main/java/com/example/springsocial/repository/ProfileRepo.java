package com.example.springsocial.repository;

import com.example.springsocial.entity.userRelated.Profile;
import com.example.springsocial.entity.userRelated.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {

    //Profile FindProfileByUser(User user);
}
