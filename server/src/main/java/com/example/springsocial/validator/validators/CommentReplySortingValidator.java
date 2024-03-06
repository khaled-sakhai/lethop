package com.example.springsocial.validator.validators;

import com.example.springsocial.util.Constants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class CommentReplySortingValidator implements ConstraintValidator<ValidCommentReplySortBy, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(Constants.AllowedCommentSorting).contains(value);
    }

}
