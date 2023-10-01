package com.example.plan;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PlanUtils {

    public PlanUtils() {
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
