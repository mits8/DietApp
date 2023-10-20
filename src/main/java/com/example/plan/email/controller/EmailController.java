package com.example.plan.email.controller;

import com.example.plan.email.service.EmailService;
import com.example.plan.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {
        return emailService.sendEmail(emailDTO);
    }

    @PostMapping("/forgotEmail")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailDTO emailDTO) throws Exception {
        return emailService.forgotPassword(emailDTO);
    }

}

