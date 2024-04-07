package com.example.springsocial.base;

import com.example.springsocial.entity.postRelated.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID extends Number> extends JpaRepository<T, ID> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM #{#entityName} WHERE id = :entityId")
    void adminDeleteById(ID entityId);


    @Query(nativeQuery = true, value = "SELECT * FROM #{#entityName} WHERE deleted = true")
    Page<T> adminFindAllDeleted(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM #{#entityName} WHERE id = :entityId")
    Optional<T> adminFindById(ID entityId);

}
