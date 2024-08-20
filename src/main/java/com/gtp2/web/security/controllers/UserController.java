package com.gtp2.web.security.controllers;


import com.gtp2.web.security.models.User;
import com.gtp2.web.security.services.UserServices;
import com.gtp2.web.security.utils.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserServices userService;

    @GetMapping
    public ResponseEntity<CustomResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(new CustomResponse<>("User retrieved successfully", HttpStatus.OK.value(), users), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<User>> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return new ResponseEntity<>(new CustomResponse<>("User created successfully", HttpStatus.CREATED.value(), createdUser), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<String>> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        String encodedFirstName = StringEscapeUtils.escapeHtml4(user.getFirstName());
        return new ResponseEntity<>(new CustomResponse<>("User retrieved successfully", HttpStatus.OK.value(), encodedFirstName), HttpStatus.OK);
    }
}
