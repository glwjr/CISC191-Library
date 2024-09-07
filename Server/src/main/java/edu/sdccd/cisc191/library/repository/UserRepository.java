package edu.sdccd.cisc191.library.repository;

import edu.sdccd.cisc191.library.model.User;

import java.io.IOException;
import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    User getUserById(String userId);
    void addUser(User user) throws IOException;
    void updateUser(User user) throws IOException;
    void deleteUser(String userId) throws IOException;
}
