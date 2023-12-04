package com.example.springsocial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.entity.userRelated.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
  Role findByName(String name);
}
