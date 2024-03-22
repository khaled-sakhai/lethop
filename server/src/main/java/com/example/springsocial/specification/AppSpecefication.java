package com.example.springsocial.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.example.springsocial.entity.Features.Notification;

public class AppSpecefication {
    

       public static Specification<Notification> notificationsOlderThan(int days ){
        return (root, query, criteriaBuilder) -> {
            Date xDaysAgo = new Date(System.currentTimeMillis() - days * 24 * 3600 * 1000); // Convert days to milliseconds
            return criteriaBuilder.lessThan(root.get("createdDate"), xDaysAgo);
        };
    }

    public static Specification<Notification> notificationsRead(boolean isRead ){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("isRead"), isRead);
        };
    }



}
