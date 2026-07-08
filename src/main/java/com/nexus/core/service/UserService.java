package com.nexus.core.service;

import com.nexus.core.model.User;
import com.nexus.core.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    // Password encoder
    private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUser(String username) {
        return userRepository.findByUsername(username);
    }

    // Password Strength Check
    public boolean isStrongPassword(String password) {
        // At least 8 chars, 1 letter, 1 number, 1 special char
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return (password != null && password.matches(regex));
    }

    public boolean addUser(User newUser) {
        if (userRepository.existsByUsername(newUser.getUsername()))
            return false;

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        userRepository.save(newUser);
        return true;
    }

    public boolean deleteUser(String username, String password) {
        if (!checkCredentials(username, password))
            return false;

        userRepository.deleteByUsername(username);
        return true;
    }

    public boolean checkCredentials(String username, String rawPassword) {
        Optional<User> userOpt = findUser(username);
        return (userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword()));
    }
}