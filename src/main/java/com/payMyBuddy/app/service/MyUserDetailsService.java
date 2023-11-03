package com.payMyBuddy.app.service;

import com.payMyBuddy.app.dto.UserDto;
import com.payMyBuddy.app.exception.EmailAlreadyExistException;
import com.payMyBuddy.app.model.User;
import com.payMyBuddy.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import java.util.List;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }
        return user;
    }

    private static List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    public User registerNewUserAccount(UserDto accountDto) throws EmailAlreadyExistException {

        User user = new User();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());

        user.setPassword((accountDto.getPassword()));

        user.setEmail(accountDto.getEmail());
//        user.setRole(new Role(Integer.valueOf(1), user));
        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
