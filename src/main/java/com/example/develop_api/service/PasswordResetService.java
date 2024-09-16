package com.example.develop_api.service;

import com.example.develop_api.entity.PasswordResetToken;
import com.example.develop_api.entity.User;
import com.example.develop_api.payload.ForgotPasswordDto;
import com.example.develop_api.payload.ResetPasswordDto;
import com.example.develop_api.repository.PasswordResetTokenRepository;
import com.example.develop_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordResetTokenRepository tokenRepository;
    private ForgotPasswordDto user;

    @Autowired
    public PasswordResetService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                EmailService emailService, PasswordResetTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<Map<String, String>> createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if a token already exists for the user
        Optional<PasswordResetToken> existingToken = tokenRepository.findByUser(user);
        existingToken.ifPresent(tokenRepository::delete);

        // Generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        // Send email with reset link
        String resetUrl = "http://localhost:8080/api/user/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);

        //return ResponseEntity.ok("Password reset link sent to your email");
        // Prepare the JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset link sent to your email");
        response.put("email", user.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, String>> resetPassword(String token, ResetPasswordDto resetPasswordDto) {

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successfully");
        //response.put("email", user.getEmail());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
