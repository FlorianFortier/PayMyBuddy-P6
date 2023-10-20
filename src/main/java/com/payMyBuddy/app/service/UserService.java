package com.payMyBuddy.app.service;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.exeption.UserAlreadyExistException;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.UserRepository;
import com.payMyBuddy.app.util.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;


@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }

        User user = new User();
        user.setName(userDto.getFirstName());
        user.setSurname(userDto.getSurname());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setRoles(Arrays.asList("ROLE_USER"));

        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}


