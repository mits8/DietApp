package com.example.plan.utils;

import com.example.plan.customerInfo.entity.CustomerInfo;
import com.example.plan.enums.Gender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

public class Utils {

    public Utils() {
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>(responseMessage, httpStatus);
    }


    public static LocalDate formatter(Map<String, Object> requestMap) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        if (requestMap.containsKey("birthday")) {
            String date = (String) requestMap.get("birthday");
            return LocalDate.parse(date, formatter);
        }
        else if (requestMap.containsKey("startDate")) {
            String startDate = (String) requestMap.get("startDate");
            return LocalDate.parse(startDate, formatter);
        }
        throw new IllegalArgumentException("No valid date field found in the requestMap");
    }

    public static LocalDate secondFormatter(Map<String, Object> requestMap) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        if (requestMap.containsKey("endDate")) {
            String endDate = (String) requestMap.get("endDate");
            return LocalDate.parse(endDate, formatter);
        }
        throw new IllegalArgumentException("No valid date field found in the requestMap");
    }

    public static double calculateTDEE(double bmr, int activityLevel) {
        double tdee;
        switch (activityLevel) {
            case 1:
                tdee = bmr * 1.2;
                break;
            case 2:
                tdee = bmr * 1.375;
                break;
            case 3:
                tdee = bmr * 1.55;
                break;
            case 4:
                tdee = bmr * 1.725;
                break;
            case 5:
                tdee = bmr * 1.9;
                break;
            default:
                throw new IllegalArgumentException("Invalid activity level");
        }
        return tdee;
    }


    public static double calculateBMR(CustomerInfo customerInfo) {
        double bmr;
        double weight = customerInfo.getWeight();
        double height = customerInfo.getHeight();
        int age = customerInfo.getAge();
        Gender gender = Gender.valueOf(String.valueOf(customerInfo.getCustomer().getGender()));
        if (gender.isMale()) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }
        return bmr;
    }

    public static double calculateTotalCalories(CustomerInfo customerInfo) {
        double bmr = calculateBMR(customerInfo); // Calculate Basal Metabolic Rate
        double tdee = calculateTDEE(bmr, customerInfo.getActivityLevel()); // Calculate Total Daily Energy Expenditure
        return tdee;
    }

    public static double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.valueOf((String) value);
        } else {
            throw new IllegalArgumentException("Invalid value for key: " + key);
        }
    }
}
