package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.dto.UserDTO;
import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getName(), user.getRole());
    }

    private User convertToUser(UserDTO userDTO) {
        return new User(userDTO.getName(), userDTO.getUserRole());
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(String userId) {
        User user = userService.getUserById(userId);
        return convertToDTO(user);
    }

    public UserDTO addUser(UserDTO newUserDTO) throws IOException {
        User newUser = convertToUser(newUserDTO);
        User addedUser = userService.addUser(newUser);
        return convertToDTO(addedUser);
    }

    public UserDTO updateUser(UserDTO updatedUserDTO) throws IOException {
        User updatedUser = convertToUser(updatedUserDTO);
        User savedUser = userService.updateUser(updatedUser);
        return convertToDTO(savedUser);
    }

    public void deleteUser(String userId) throws IOException {
        userService.deleteUser(userId);
    }
}
