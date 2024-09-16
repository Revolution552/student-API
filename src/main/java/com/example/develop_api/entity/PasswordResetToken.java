package com.example.develop_api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime createdDate;

    public PasswordResetToken() {
        this.createdDate = LocalDateTime.now();
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.createdDate = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }


    // Getters and setters
}