package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.Features.Notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends BaseRepository<Notification,Long> {

    Page<Notification> findByUserIdAndIsDeliveredAndIsRead(Long userId, boolean isDelivered, boolean isRead, Pageable pageable);

}
