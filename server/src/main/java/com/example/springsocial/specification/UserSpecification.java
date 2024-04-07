package com.example.springsocial.specification;

import com.example.springsocial.entity.Features.Notification;
import com.example.springsocial.entity.postRelated.Post;
import com.example.springsocial.entity.postRelated.Tag;
import com.example.springsocial.entity.userRelated.Role;
import com.example.springsocial.entity.userRelated.User;
import com.example.springsocial.enums.APPRole;
import com.example.springsocial.enums.AuthProvider;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class UserSpecification {


    public static Specification<User> byProvider(String provider ){

        return (root, query, criteriaBuilder) -> {
            if(provider==null || provider.isBlank() || provider.length()<5){
                return null;
            }
            try {
                AuthProvider authProvider = AuthProvider.valueOf(provider.toLowerCase());
                return criteriaBuilder.equal(root.get("provider"), authProvider);
            } catch (IllegalArgumentException e) {
                return null;
            }
        };
    }

    public static Specification<User> byActive(Boolean isActive ){

        return (root, query, criteriaBuilder) -> {
            if(isActive==null){
                return null;
            }
            return criteriaBuilder.equal(root.get("isActive"), isActive);
        };
    }

    public static Specification<User> byProfileUpdated(Boolean needProfileUpdate ){

        return (root, query, criteriaBuilder) -> {
            if(needProfileUpdate==null){
                return null;
            }
            return criteriaBuilder.equal(root.get("needProfileUpdate"), needProfileUpdate);
        };
    }

    public static Specification<User> byUserId(Long userId){

        return (root, query, criteriaBuilder) -> {
            if(userId==null){
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), userId);
        };
    }

    public static Specification<User> byRole(String role){
        return(
                Root<User> root,
                CriteriaQuery<?> criteriaQuery,
                CriteriaBuilder builder) ->{
            if (role == null || role.isBlank()) {
                return null;
            }
            APPRole roleEnum=APPRole.valueOf(role.toUpperCase());
            Join<User, Role> userJoin = root.join("roles");
            return builder.equal(userJoin.get("name"),roleEnum);
        };
    }




}
