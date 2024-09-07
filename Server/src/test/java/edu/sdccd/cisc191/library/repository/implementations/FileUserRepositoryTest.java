package edu.sdccd.cisc191.library.repository.implementations;

import edu.sdccd.cisc191.library.exceptions.UserNotFoundException;
import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileUserRepositoryTest {
    private FileUserRepository userRepository;

    @BeforeEach
    public void setUp() throws IOException {
        Path tempFile = Files.createTempFile("users", ".json");
        userRepository = new FileUserRepository(tempFile);
    }

    @Test
    public void testAddUser() throws IOException {
        User user = new User("User", UserRole.MEMBER);

        userRepository.addUser(user);

        User retrievedUser = userRepository.getUserById(user.getUserId());

        assertNotNull(retrievedUser);
        assertEquals(user.getUserId(), retrievedUser.getUserId());
    }

    @Test
    public void testGetAllUsers() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        assertEquals(2, userRepository.getAllUsers().size());
    }

    @Test
    public void testUpdateUser() throws IOException {
        User user = new User("User", UserRole.MEMBER);

        userRepository.addUser(user);

        user.setName("New Name");

        userRepository.updateUser(user);

        User updatedUser = userRepository.getUserById(user.getUserId());

        assertEquals("New Name", updatedUser.getName());
    }

    @Test
    public void testDeleteUser() throws IOException {
        User user1 = new User("User1", UserRole.MEMBER);
        User user2 = new User("User2", UserRole.MEMBER);

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        assertEquals(2, userRepository.getAllUsers().size());

        userRepository.deleteUser(user1.getUserId());

        assertThrows(UserNotFoundException.class, () -> userRepository.getUserById(user1.getUserId()));
        assertEquals(1, userRepository.getAllUsers().size());
    }
}
