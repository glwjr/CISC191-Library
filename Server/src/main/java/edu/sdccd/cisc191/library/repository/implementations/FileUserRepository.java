package edu.sdccd.cisc191.library.repository.implementations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.sdccd.cisc191.library.exceptions.UserFileException;
import edu.sdccd.cisc191.library.exceptions.UserNotFoundException;
import edu.sdccd.cisc191.library.model.User;
import edu.sdccd.cisc191.library.repository.UserRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUserRepository implements UserRepository {
    private final Path filePath;
    private final Gson gson = new Gson();

    public FileUserRepository(Path filePath) {
        this.filePath = filePath;
        initializeFile();
    }

    private void initializeFile() {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                saveAllUsers(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new UserFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (FileReader reader = new FileReader(filePath.toFile())) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, userListType);
            return (users != null ? users : new ArrayList<>());
        } catch (IOException e) {
            throw new UserFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public User getUserById(String userId) {
        return getAllUsers().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    @Override
    public User addUser(User user) throws IOException {
        List<User> users = getAllUsers();
        if (users.stream().anyMatch(u -> u.getUserId().equals(user.getUserId()))) {
            throw new IllegalArgumentException("User with ID " + user.getUserId() + " already exists");
        }
        users.add(user);
        saveAllUsers(users);
        return user;
    }

    @Override
    public User updateUser(User updatedUser) throws IOException {
        List<User> users = getAllUsers();
        boolean userFound = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updatedUser.getUserId())) {
                users.set(i, updatedUser);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            throw new IllegalArgumentException("User with ID " + updatedUser.getUserId() + " not found");
        }

        saveAllUsers(users);
        return updatedUser;
    }

    @Override
    public void deleteUser(String userId) throws IOException {
        List<User> users = getAllUsers();
        boolean userRemoved = users.removeIf(user -> user.getUserId().equals(userId));

        if (!userRemoved) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        saveAllUsers(users);
    }

    private void saveAllUsers(List<User> users) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            throw new UserFileException("Failed to save users to file: " + filePath, e);
        }
    }
}
