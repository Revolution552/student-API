package com.example.develop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.develop_api.entity.User;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by email
    Optional<User> findByEmail(String email);

    // Find a user by username
    Optional<User> findByUsername(String username);

    // Check if a username exists
    Boolean existsByUsername(String username);

    // Check if an email exists
    Boolean existsByEmail(String email);

}
