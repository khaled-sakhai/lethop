package com.example.springsocial.service.permessions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
//@PreAuthorize("hasAuthority('ADMIN') or #entity.user.email == authentication.name")
@PreAuthorize("#entity.user.email == authentication.name")

public @interface EntityOwner {
    Class<?> value();


}
