package edu.sdccd.cisc191.library.dto;

import edu.sdccd.cisc191.library.model.UserRole;

public class UserDTO {
    private String name;
    private UserRole userRole;

    public UserDTO(String name, UserRole userRole) {
        this.name = name;
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
