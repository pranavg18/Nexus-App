package com.nexus.core.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexus.core.model.User;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UserService {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.json");
    private final ObjectMapper objectMapper;

    // Session tracker
    private final Set<String> loggedInUsers = new HashSet<>();

    public UserService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private void createDir() throws IOException {
        if (!Files.exists(DATA_DIR)) Files.createDirectories(DATA_DIR);
    }

    public List<User> getAllUsers() throws IOException {
        createDir();
        File file = USERS_FILE.toFile();
        if (!file.exists()) return new ArrayList<>();
        return objectMapper.readValue(file, new TypeReference<List<User>>() {});
    }

    private void saveAllUsers(List<User> users) throws IOException {
        createDir();
        objectMapper.writeValue(USERS_FILE.toFile(), users);
    }

    public Optional<User> findUser(String username) throws IOException {
        List<User> allUsers = getAllUsers();
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) return Optional.of(user);
        }
        return Optional.empty();
    }

    // Password Strength Check
    public boolean isStrongPassword(String password) {
        // At least 8 chars, 1 letter, 1 number, 1 special char
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password != null && password.matches(regex);
    }

    public boolean addUser(User newUser) throws IOException {
        List<User> allUsers = getAllUsers();
        for (User user : allUsers) {
            if (user.getUsername().equals(newUser.getUsername())) return false; // User exists
        }
        allUsers.add(newUser);
        saveAllUsers(allUsers);
        return true;
    }

    public boolean deleteUser(String username, String password) throws IOException {
        List<User> allUsers = getAllUsers();
        User target = null;
        for (User user : allUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                target = user;
                break;
            }
        }
        if (target != null) {
            allUsers.remove(target);
            saveAllUsers(allUsers);
            loggedInUsers.remove(username); // Force logout
            return true;
        }
        return false;
    }

    public boolean login(String username, String password) throws IOException {
        if (checkCredentials(username, password)) {
            loggedInUsers.add(username);
            return true;
        }
        return false;
    }

    public void logout(String username) {
        loggedInUsers.remove(username);
    }

    public boolean isLoggedIn(String username) {
        return loggedInUsers.contains(username);
    }

    public boolean checkCredentials(String username, String password) throws IOException {
        Optional<User> userOpt = findUser(username);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }
}