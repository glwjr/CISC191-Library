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
import java.util.HashMap;
import java.util.Map;

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
                saveAllUsers(new HashMap<>());
            }
        } catch (IOException e) {
            throw new UserFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public Map<String, User> getAllUsers() {
        try (FileReader reader = new FileReader(filePath.toFile())) {
            Type userMapType = new TypeToken<HashMap<String, User>>() {}.getType();
            HashMap<String, User> users = gson.fromJson(reader, userMapType);
            return (users != null ? users : new HashMap<>());
        } catch (IOException e) {
            throw new UserFileException("Failed to initialize file: " + filePath, e);
        }
    }

    @Override
    public User getUserById(String userId) {
        Map<String, User> users = getAllUsers();
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        return user;
    }

    @Override
    public User addUser(User user) throws IOException {
        Map<String, User> users = getAllUsers();
        if (users.containsKey(user.getUserId())) {
            throw new UserNotFoundException("User with ID " + user.getUserId() + " already exists");
        }
        users.put(user.getUserId(), user);
        saveAllUsers(users);
        return user;
    }

    @Override
    public User updateUser(User updatedUser) throws IOException {
        Map<String, User> users = getAllUsers();
        if (!users.containsKey(updatedUser.getUserId())) {
            throw new UserNotFoundException("User with ID " + updatedUser.getUserId() + " not found");
        }
        users.put(updatedUser.getUserId(), updatedUser);
        saveAllUsers(users);
        return updatedUser;
    }

    @Override
    public void deleteUser(String userId) throws IOException {
        Map<String, User> users = getAllUsers();
        if (users.remove(userId) == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        users.remove(userId);
        saveAllUsers(users);
    }

    private void saveAllUsers(Map<String, User> users) throws IOException {
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            throw new UserFileException("Failed to save users to file: " + filePath, e);
        }
    }
}
