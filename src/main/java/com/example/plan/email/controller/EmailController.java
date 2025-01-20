package com.example.plan.email.controller;

import com.example.plan.dto.email.EmailRequest;
import com.example.plan.email.service.EmailService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<ResponseMessage> sendEmail(@RequestBody EmailRequest emailRequest) {
        return emailService.sendEmail(emailRequest);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestBody Map<String, Object> requestMap) throws Exception {
        return emailService.forgotPassword(requestMap);
    }

}

