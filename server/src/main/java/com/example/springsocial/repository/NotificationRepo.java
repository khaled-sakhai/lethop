package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.entity.postRelated.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface NotificationRepo extends BaseRepository<Notification,Long>, JpaSpecificationExecutor<Notification>  {

    Page<Notification> findByUserIdAndIsRead(Long userId, boolean isRead, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification p WHERE p IN :notifs")
    void deleteAllNotifications(List<Notification> notifs);

    
}
