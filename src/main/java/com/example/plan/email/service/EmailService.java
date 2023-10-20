package com.example.plan.email.service;

import com.example.plan.dto.EmailDTO;
import org.springframework.http.ResponseEntity;

public interface EmailService {


    ResponseEntity<String> sendEmail(EmailDTO emailDTO);

    ResponseEntity<String> forgotPassword(EmailDTO emailDTO) throws Exception;
}
