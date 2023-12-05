package com.example.springsocial.repository;

import com.example.springsocial.entity.userRelated.Profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {

}
