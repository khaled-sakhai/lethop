package com.example.springsocial.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID extends Number> extends JpaRepository<T, ID> {
}
