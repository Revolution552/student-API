package com.example.develop_api.repository;

import com.example.develop_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Custom query to find a role by its name
    Optional<Role> findByName(String name);
}
