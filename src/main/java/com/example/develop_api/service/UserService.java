package com.example.develop_api.service;

import com.example.develop_api.entity.Role;
import com.example.develop_api.entity.User;
import com.example.develop_api.repository.RoleRepository;
import com.example.develop_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public User saveOrUpdateUser(User user) {
        // Ensure the role is managed
        Set<Role> managedRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            managedRoles.add(roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role not found")));
        }
        user.setRoles(managedRoles);
        return userRepository.save(user);
    }
}
