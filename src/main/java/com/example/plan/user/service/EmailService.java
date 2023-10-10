package com.example.plan.user.service;

import com.example.plan.user.dto.EmailDTO;
import org.springframework.http.ResponseEntity;

public interface EmailService {


    ResponseEntity<String> sendEmail(EmailDTO emailDTO);
}
