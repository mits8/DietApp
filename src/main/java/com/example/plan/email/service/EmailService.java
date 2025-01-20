package com.example.plan.email.service;

import com.example.plan.dto.email.EmailRequest;
import com.example.plan.utils.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface EmailService {


    ResponseEntity<ResponseMessage> sendEmail(EmailRequest emailRequest);

    ResponseEntity<ResponseMessage> forgotPassword(Map<String, Object> requestMap) throws Exception;
}
