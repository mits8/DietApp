package com.example.plan.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PlanUtils {

    public PlanUtils() {
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>(responseMessage, httpStatus);
    }


    public static LocalDate formatter(Map<String, String> requestMap) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (requestMap.containsKey("birthday")) {
            String date = requestMap.get("birthday");
            return LocalDate.parse(date, formatter);
        }
        if (requestMap.containsKey("startDate")) {
            String startDate = requestMap.get("startDate");
            return LocalDate.parse(startDate, formatter);
        }
        if (requestMap.containsKey("endDate")) {
            String endDate = requestMap.get("endDate");
            return LocalDate.parse(endDate, formatter);
        }
        throw new IllegalArgumentException("No valid date field found in the requestMap");
    }
}
