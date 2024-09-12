package edu.sdccd.cisc191.library.service;

import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.repository.UserRepository;

import java.io.IOException;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    public User addUser(User user) throws IOException {
        return userRepository.addUser(user);
    }

    public User updateUser(User user) throws IOException {
        return userRepository.updateUser(user);
    }

    public void deleteUser(String userId) throws IOException {
        userRepository.deleteUser(userId);
    }
}
