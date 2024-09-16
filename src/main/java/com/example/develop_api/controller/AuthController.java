package com.example.develop_api.controller;

import com.example.develop_api.entity.Role;
import com.example.develop_api.entity.User;
import com.example.develop_api.payload.ForgotPasswordDto;
import com.example.develop_api.payload.LoginDto;
import com.example.develop_api.payload.ResetPasswordDto;
import com.example.develop_api.payload.SignUpDto;
import com.example.develop_api.repository.RoleRepository;
import com.example.develop_api.repository.UserRepository;
import com.example.develop_api.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody LoginDto loginDto) {
        Map<String, String> response = new HashMap<>();

        try {
            System.out.println("Username: " + loginDto.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.put("message", "User signed-in successfully!");
            response.put("status", "OK");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            System.err.println("Invalid credentials: " + e.getMessage());

            response.put("message", "Invalid username or password!");
            response.put("status", "UNAUTHORIZED");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());

            response.put("message", "Authentication failed: " + e.getMessage());
            response.put("status", "INTERNAL_SERVER_ERROR");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody SignUpDto signUpDto) {
        // Create a response map to return as JSON
        Map<String, String> response = new HashMap<>();

        // Check if username exists in the database
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            response.put("message", "Username is already taken!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Check if email exists in the database
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            response.put("message", "Email is already taken!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // Create a user object
            User user = new User();
            user.setName(signUpDto.getName());
            user.setUsername(signUpDto.getUsername());
            user.setEmail(signUpDto.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            user.setBirthday(signUpDto.getBirthday());
            user.setCourse(signUpDto.getCourse());
            user.setAge(signUpDto.getAge());

            // Fetch the role and assign it to the user
            Role role = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            // Ensure the role is managed by the persistence context
            user.setRoles(Collections.singleton(role));

            // Save the user to the database
            userRepository.save(user);

            response.put("message", "User registered successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Registration failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return passwordResetService.createPasswordResetToken(forgotPasswordDto.getEmail());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam("token") String token,
                                                @RequestBody ResetPasswordDto resetPasswordDto) {
        return passwordResetService.resetPassword(token, resetPasswordDto);
    }

    private final PasswordResetService passwordResetService;

    @Autowired
    public AuthController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }


}
