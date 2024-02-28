package com.example.springsocial.service.permessions;

import com.example.springsocial.entity.postRelated.Post;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//@PreAuthorize("hasAuthority('ADMIN') or {hasRole('USER') && #post.user.email == authentication.name }")

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('ADMIN') or {hasRole('USER') && #post.user.email == authentication.name }")
public @interface PostOwner {

}
