package com.example.springsocial.validator.permessions;

import com.example.springsocial.entity.postRelated.Post;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('ROLE_ADMIN') or (hasAuthority('ROLE_USER') && #post.user.email == authentication.name )")
public @interface PostOwner {

}
