package com.example.develop_api.config;

import com.example.develop_api.entity.Role;
import com.example.develop_api.entity.User;
import com.example.develop_api.repository.RoleRepository;
import com.example.develop_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            // Create and save roles
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            Role userRole = new Role();
            userRole.setName("ROLE_USER");

            // Check if roles already exist before saving
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                roleRepository.save(adminRole);
            }
            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                roleRepository.save(userRole);
            }

            // Create and save default users
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(roleRepository.findByName("ROLE_ADMIN").get()));
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setName("User");
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRoles(Set.of(roleRepository.findByName("ROLE_USER").get()));
                userRepository.save(user);
            }
        };
    }
}
