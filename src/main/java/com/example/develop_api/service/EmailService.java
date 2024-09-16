package com.example.develop_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("zengo.elias@thelyntel.co.tz"); // optional, you can set a default "from" address

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String email, String resetUrl) {
        // Implement the email sending logic here
        System.out.println("Sending password reset email to " + email + " with URL: " + resetUrl);
        sendSimpleEmail(email, "Password reset link", resetUrl);
    }
}
