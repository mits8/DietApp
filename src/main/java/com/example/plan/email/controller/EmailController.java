package com.example.plan.email.controller;

import com.example.plan.email.service.EmailService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<ResponseMessage> sendEmail(@RequestBody Map<String, Object> requestMap) {
        return emailService.sendEmail(requestMap);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestBody Map<String, Object> requestMap) throws Exception {
        return emailService.forgotPassword(requestMap);
    }

}

