package com.example.springsocial.validator.validators;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = CommentReplySortingValidator.class)
public @interface ValidCommentReplySortBy {
    String message() default "Invalid sort parameter. Allowed values: createdDate,lastModifiedDate";
}
