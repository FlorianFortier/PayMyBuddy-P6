package com.payMyBuddy.app.util;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.exception.UserAlreadyExistException;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.model.VerificationToken;

public interface IUserService {

    User registerNewUserAccount(UserDto userDto)
            throws UserAlreadyExistException;

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    void saveVerificationToken(VerificationToken verificationToken);

    User findByUsername(String username);
}