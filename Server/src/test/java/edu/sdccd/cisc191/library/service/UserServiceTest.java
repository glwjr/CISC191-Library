package edu.sdccd.cisc191.library.service;

import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.model.UserRole;
import edu.sdccd.cisc191.library.repository.UserRepository;
import edu.sdccd.cisc191.library.repository.implementations.FileUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void setUp() throws IOException {
        Path usersTempFile = Files.createTempFile("users", ".json");

        UserRepository userRepository = new FileUserRepository(usersTempFile);

        userService = new UserService(userRepository);
    }

    @Test
    public void testGetAllUsers() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);

        userService.addUser(user1);
        userService.addUser(user2);

        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    public void testAddUser() throws IOException {
        User user = new User("User", UserRole.MEMBER);

        userService.addUser(user);

        assertEquals(1, userService.getAllUsers().size());
        assertEquals(user.getName(), userService.getUserById(user.getUserId()).getName());
    }

    @Test
    public void testUpdateUser() throws IOException {
        User user = new User("User", UserRole.MEMBER);

        userService.addUser(user);

        user.setName("New Name");

        userService.updateUser(user);

        assertEquals(user.getName(), userService.getUserById(user.getUserId()).getName());
    }

    @Test
    public void testDeleteUser() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);

        userService.addUser(user1);
        userService.addUser(user2);

        assertEquals(2, userService.getAllUsers().size());

        userService.deleteUser(user1.getUserId());

        assertEquals(1, userService.getAllUsers().size());
    }
}
