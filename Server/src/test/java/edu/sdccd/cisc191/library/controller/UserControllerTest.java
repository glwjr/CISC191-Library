package edu.sdccd.cisc191.library.controller;

import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.model.UserRole;
import edu.sdccd.cisc191.library.repository.UserRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileUserRepository;
import edu.sdccd.cisc191.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void setUp() throws IOException {
        Path usersTempFile = Files.createTempFile("users", ".json");

        UserRepository userRepository = new FileUserRepository(usersTempFile);
        UserService userService = new UserService(userRepository);
        userController = new UserController(userService);
    }

    @Test
    public void testGetAllUsers() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);
        User user3 = new User("User3", UserRole.MEMBER);

        userController.addUser(user1);
        userController.addUser(user2);
        userController.addUser(user3);

        Map<String, User> users = userController.getAllUsers();
        assertEquals(3, users.size());
    }

    @Test
    public void testGetUserById() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);

        userController.addUser(user1);

        assertEquals("User1", userController.getUserById(user1.getUserId()).getName());
        assertEquals(UserRole.MEMBER, userController.getUserById(user1.getUserId()).getRole());
    }

    @Test
    public void testAddUser() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.LIBRARIAN);

        userController.addUser(user1);
        userController.addUser(user2);

        assertEquals("User1", userController.getUserById(user1.getUserId()).getName());
        assertEquals(UserRole.MEMBER, userController.getUserById(user1.getUserId()).getRole());
        assertEquals("User2", userController.getUserById(user2.getUserId()).getName());
        assertEquals(UserRole.LIBRARIAN, userController.getUserById(user2.getUserId()).getRole());
    }

    @Test
    public void testUpdateUser() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);

        userController.addUser(user1);

        user1.setName("New Name");
        user1.setRole(UserRole.LIBRARIAN);

        userController.updateUser(user1);

        assertEquals("New Name", userController.getUserById(user1.getUserId()).getName());
        assertEquals(UserRole.LIBRARIAN, userController.getUserById(user1.getUserId()).getRole());
    }

    @Test
    public void testDeleteUser() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);

        userController.addUser(user1);
        userController.addUser(user2);

        userController.deleteUser(user1.getUserId());

        Map<String, User> users = userController.getAllUsers();
        assertEquals(1, users.size());
    }
}
