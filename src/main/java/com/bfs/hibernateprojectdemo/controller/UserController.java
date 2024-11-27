package com.bfs.hibernateprojectdemo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.DataResponse;
import com.bfs.hibernateprojectdemo.dto.LoginRequest;
import com.bfs.hibernateprojectdemo.dto.LoginResponse;
import com.bfs.hibernateprojectdemo.dto.UserDTO;
import com.bfs.hibernateprojectdemo.exception.InvalidCredentialsException;
import com.bfs.hibernateprojectdemo.security.JwtProvider;
import com.bfs.hibernateprojectdemo.service.UserService;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<DataResponse> registerUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    DataResponse.builder()
                            .success(false)
                            .message("Validation failed")
                            .data(result.getAllErrors())
                            .build()
            );
        }

        try {
            UserDTO registeredUserDTO = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    DataResponse.builder()
                            .success(true)
                            .message("User registered successfully")
                            .data(registeredUserDTO)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    DataResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    DataResponse.builder()
                            .success(false)
                            .message("An unexpected error occurred.")
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<DataResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            UserDTO userDTO = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            String token = jwtProvider.generateToken(userDTO);

            LoginResponse loginResponse = new LoginResponse(userDTO, token);

            return ResponseEntity.ok(
                    DataResponse.builder()
                            .success(true)
                            .message("Login successful")
                            .data(loginResponse)
                            .build()
            );
        } catch (InvalidCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    DataResponse.builder()
                            .success(false)
                            .message("Invalid credentials")
                            .data(null)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    DataResponse.builder()
                            .success(false)
                            .message("An unexpected error occurred.")
                            .build()
            );
        }
    }
}
