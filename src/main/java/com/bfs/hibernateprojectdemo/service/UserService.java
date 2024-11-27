package com.bfs.hibernateprojectdemo.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bfs.hibernateprojectdemo.dao.UserDao;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.UserDTO;
import com.bfs.hibernateprojectdemo.exception.InvalidCredentialsException;

@Service
@Transactional
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(User user) {
        userDao.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already exists");
        });
        userDao.findUserByEmail(user.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already exists");
        });

        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.registerUser(user);

        // Return UserDTO using the fromUser method
        return UserDTO.fromUser(user);
    }

    public UserDTO authenticateUser(String username, String password) throws InvalidCredentialsException {
        User user = userDao.findUserByUsername(username)
                           .orElseThrow(() -> new InvalidCredentialsException("Incorrect credentials, please try again."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Incorrect credentials, please try again.");
        }
        
        return UserDTO.fromUser(user); 
    }

    public UserDTO findUserById(int userId) {
        User user = userDao.findUserById(userId)
                           .orElseThrow(() -> new InvalidCredentialsException("User not found with ID: " + userId));
        
        return UserDTO.fromUser(user);
    }
}
