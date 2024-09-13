package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.service.UserService;

import java.io.IOException;
import java.util.Map;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public Map<String, User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User getUserById(String userId) {
        return userService.getUserById(userId);
    }

    public User addUser(User user) throws IOException {
        return userService.addUser(user);
    }

    public User updateUser(User user) throws IOException {
        return userService.updateUser(user);
    }

    public void deleteUser(String userId) throws IOException {
        userService.deleteUser(userId);
    }
}
