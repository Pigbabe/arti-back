package com.example.artist.controller;

import com.example.artist.domain.User;
import com.example.artist.service.S3Service;
import com.example.artist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private S3Service s3Service;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        System.out.println("Received user: " + user); // 로그 추가
        User savedUser = userService.registerUser(user);
        if(savedUser.getIsArtist()) {
            s3Service.createFolder(savedUser.getName()); //S3 폴더 추가
        }
        System.out.println("User registered successfully: " + savedUser); // 로그 추가
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        boolean isValidUser = userService.checkLogin(user.getUsername(), user.getPassword());
        if (isValidUser) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}