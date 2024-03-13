package com.example.springsocial.repository;

import com.example.springsocial.base.BaseRepository;
import com.example.springsocial.entity.Notification.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends BaseRepository<Notification,Long> {

    List<Notification> findByUserIdAndIsDeliveredAndIsRead(Long userId, boolean isDelivered, boolean isRead);

}
