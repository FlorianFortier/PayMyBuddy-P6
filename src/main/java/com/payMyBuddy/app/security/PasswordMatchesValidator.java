package com.payMyBuddy.app.security;

import com.payMyBuddy.app.dto.UserDTO;
import com.payMyBuddy.app.security.annotation.PasswordMatches;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserDTO user = (UserDTO) obj;
        return user.getPassword().equals(user.getMatchingPwd());
    }
}