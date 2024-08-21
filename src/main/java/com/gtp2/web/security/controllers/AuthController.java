package com.gtp2.web.security.controllers;


import com.gtp2.web.security.dto.LoginDto;
import com.gtp2.web.security.models.User;
import com.gtp2.web.security.services.AuthService;
import com.gtp2.web.security.services.UserServices;
import com.gtp2.web.security.utils.CustomResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.util.Optional;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {


    @Autowired
    private AuthService authService;
    private UserServices userService;


    @PostMapping("/signup")
    public ResponseEntity<CustomResponse<User>> register(@Valid @RequestBody User user) throws AuthenticationException {
        User createdUser = userService.saveUser(user);
        return new ResponseEntity<>(new CustomResponse<>("User registered successfully", HttpStatus.CREATED.value(), createdUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<User>> login(@Valid @RequestBody LoginDto userCredentials, HttpSession session) {
        Optional<User> user = authService.authenticate(userCredentials.getEmail(), userCredentials.getPassword());
        if (user.isPresent()) {
            session.setAttribute("userId", user.get().getId());
            session.setAttribute("username", user.get().getEmail());
            session.setAttribute("role", user.get().getRole());
            return new ResponseEntity<>(new CustomResponse<>("User logged in", HttpStatus.OK.value(), user.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CustomResponse<>("Invalid username or password", HttpStatus.UNAUTHORIZED.value(), null), HttpStatus.UNAUTHORIZED);
    }
}
