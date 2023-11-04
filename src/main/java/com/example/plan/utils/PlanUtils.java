package com.example.plan.utils;

import com.example.plan.plan.entity.Plan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PlanUtils {

    public PlanUtils() {
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>(responseMessage, httpStatus);
    }

}
