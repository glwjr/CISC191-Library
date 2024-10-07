package edu.sdccd.cisc191.library.model;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private final String userId;
    private String name;
    private UserRole role;

    public User(String name, UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
