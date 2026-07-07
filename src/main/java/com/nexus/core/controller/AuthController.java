package com.nexus.core.controller;

import com.nexus.core.model.User;
import com.nexus.core.security.JwtUtil;
import com.nexus.core.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) throws IOException {
        if (user.getUsername() == null || user.getUsername().isBlank())
            return ResponseEntity.badRequest().body("Username cannot be blank");
        // Strong password check
        if (!userService.isStrongPassword(user.getPassword()))
            return ResponseEntity.badRequest().body("Password too weak! Must contain letters, numbers, and special chars.");

        boolean added = userService.addUser(user);
        if (added)
            return ResponseEntity.ok("User registered successfully");
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) throws IOException {
        boolean isCorrect = userService.checkCredentials(user.getUsername(), user.getPassword());
        if (isCorrect) {
            // Give them the token
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String username) {
        // Since it's stateless, the server doesn't track logins, and the client just deletes the token from their browser
        return ResponseEntity.ok("Logged out successfully. (Please delete your token on the client side)");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody User user) throws IOException {
        boolean deleted = userService.deleteUser(user.getUsername(), user.getPassword());
        if (deleted)
            return ResponseEntity.ok("User deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deletion failed. Check credentials.");
    }
}