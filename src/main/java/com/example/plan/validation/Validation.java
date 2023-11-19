package com.example.plan.validation;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Validation {

    public boolean isValidNameLengthAndLetters(String firstName, String lastName) {
        int lengthFirstName = firstName.trim().length();
        int lengthLastName = lastName.trim().length();

        boolean isLengthValid = (lengthFirstName >= 5 && lengthFirstName < 30) && (lengthLastName >= 5 && lengthLastName < 30);
        boolean isLettersValid = firstName.matches("^[\\p{L}\\p{M}]+$") && lastName.matches("^[\\p{L}\\p{M}]+$");

        return isLengthValid && isLettersValid;
    }

    public boolean isValidNameLengthCustomer(Map<String, Object> requestMap) {
        return isValidNameLengthAndLetters((String) requestMap.get("firstName"), (String) requestMap.get("lastName"));
    }

    public boolean isValidPhone(String phone) {
        int phoneLength = phone.length();
        boolean isValidNumbers = phone.matches("^[0-9]+$");
        boolean isValidLength = phoneLength == 10;
        return isValidNumbers && isValidLength;
    }

    public boolean isValidNumbersAndLengthCustomer(Map<String, Object> requestMap) {
        return isValidPhone((String) requestMap.get("phone"));
    }

    public boolean isValidFoodLetters(String lettersField) {
        if (!lettersField.equals(null) || !lettersField.isEmpty()) {
            boolean isValidFieldLetters = lettersField.matches("^[\\p{L}\\p{M}]+$");
            return isValidFieldLetters;
        }
        return false;
    }

    public boolean isValidFoodNumbers(Double numbersField) {
        if (numbersField == null) {
            return false;
        }
            String numbersFieldAsString = String.valueOf(numbersField);
        boolean isValidFieldNumbers = numbersFieldAsString.matches("^[0-9,\\.]+$");
        return isValidFieldNumbers;
    }


    public boolean isValidFieldLetters(Map<String, Object> requestMap) {
        return isValidFoodLetters((String) requestMap.get("name"));
    }

    public boolean isValidFieldNumbers(Map<String, Object> requestMap) {
        return isValidFoodNumbers((Double) getDoubleValue(requestMap.get("gram"))) && isValidFoodNumbers((Double) getDoubleValue(requestMap.get("calories")));
    }

    private Object getDoubleValue(Object value) {
        if (value instanceof Double) {
            return  value;

        }
        return null;
    }
}
