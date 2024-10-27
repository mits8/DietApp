package com.example.plan.validation;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Validation {

    public boolean isValidNameLengthAndLetters(String firstname, String surname) {
        int lengthfirstname = firstname.trim().length();
        int lengthsurname = surname.trim().length();

        boolean isLengthValid = (lengthfirstname >= 5 && lengthfirstname < 30) && (lengthsurname >= 5 && lengthsurname < 30);
        boolean isLettersValid = firstname.matches("^[\\p{L}\\p{M}]+$") && surname.matches("^[\\p{L}\\p{M}]+$");

        return isLengthValid && isLettersValid;
    }

    public boolean isValidNameLengthCustomer(Map<String, Object> requestMap) {
        return isValidNameLengthAndLetters((String) requestMap.get("firstname"), (String) requestMap.get("surname"));
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
        return isValidFoodNumbers((Double) getDoubleValue(requestMap.get("calories")));
    }

    private Object getDoubleValue(Object value) {
        if (value instanceof Double) {
            return  value;

        }
        return null;
    }
}
