package com.gtp2.web.security.controllers;


import com.gtp2.web.security.dto.RegisterUserDto;
import com.gtp2.web.security.models.User;
import com.gtp2.web.security.services.UserServices;
import com.gtp2.web.security.utils.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
//@Validated
public class UserController {

    @Autowired
    private UserServices userService;

    @GetMapping
    public ResponseEntity<CustomResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(new CustomResponse<>("Users retrieved successfully", HttpStatus.OK.value(), users), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<?>> createUser(@Valid @RequestBody RegisterUserDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            CustomResponse<Map<String, String>> response = new CustomResponse<>(
                    "Validation failed",
                    HttpStatus.BAD_REQUEST.value(),
                    errors
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!(user.getFirstName() instanceof String)) {
            Map<String, String> errors = new HashMap<>();
            errors.put("firstName", "First name must be a valid string");

            CustomResponse<Map<String, String>> response = new CustomResponse<>(
                    "Validation failed",
                    HttpStatus.BAD_REQUEST.value(),
                    errors
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (userService.emailExists(user.getEmail())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("email", "Email already exists");

            CustomResponse<Map<String, String>> response = new CustomResponse<>(
                    "Validation failed",
                    HttpStatus.BAD_REQUEST.value(),
                    errors
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setAddress(user.getAddress());
        User createdUser = userService.saveUser(newUser);
        return new ResponseEntity<>(new CustomResponse<>("User created successfully", HttpStatus.CREATED.value(), createdUser), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<User>> getUserById(@PathVariable String id) throws AuthenticationException {
        Optional<User> user = userService.getUserById(id);
//        String encodedFirstName = StringEscapeUtils.escapeHtml4(user.getFirstName());
        return new ResponseEntity<>(new CustomResponse<>("User retrieved successfully", HttpStatus.OK.value(), user.get()), HttpStatus.OK);
    }
}
