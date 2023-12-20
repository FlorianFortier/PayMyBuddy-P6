package com.payMyBuddy.app.service;

import com.payMyBuddy.app.dto.UserDTO;
import com.payMyBuddy.app.exception.UserAlreadyExistException;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.model.VerificationToken;
import com.payMyBuddy.app.repository.UserRepository;
import com.payMyBuddy.app.repository.VerificationTokenRepository;
import com.payMyBuddy.app.util.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public User registerNewUserAccount(UserDTO userDto) throws UserAlreadyExistException {

        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with that email adress: "
                            + userDto.getEmail());
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        user.setRoles(Collections.singletonList("USER"));

        return userRepository.save(user);
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }


    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public void saveVerificationToken(VerificationToken verificationToken) {
        tokenRepository.save(verificationToken);
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }
}