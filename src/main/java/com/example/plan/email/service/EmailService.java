package com.example.plan.email.service;

import com.example.plan.security.auth.AuthRequest;
import com.example.plan.user.dto.EmailDTO;
import org.springframework.http.ResponseEntity;

public interface EmailService {


    ResponseEntity<String> sendEmail(EmailDTO emailDTO);

    ResponseEntity<String> forgotPassword(EmailDTO emailDTO);
}
