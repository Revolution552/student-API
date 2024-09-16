package com.example.develop_api.controller;

import org.springframework.security.core.userdetails.User;

public class UserDTO {
    private String username;
    private boolean roles;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.roles = user.getClass().isArray();
    }

    public UserDTO(com.example.develop_api.entity.User user) {

    }

    // getters and setters
}
