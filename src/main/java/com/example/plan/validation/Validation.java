package com.example.plan.validation;

import com.example.plan.dto.customer.CustomerPlanDTO;
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

    public boolean isValidNameLengthCustomerDTO(Map<String, String> requestMap) {
        return isValidNameLengthAndLetters(requestMap.get("firstName"), requestMap.get("lastName"));
    }

    public boolean isValidNameLengthCustomerPlanDTO(CustomerPlanDTO customerPlanDTO) {
        return isValidNameLengthAndLetters(customerPlanDTO.getFirstName(), customerPlanDTO.getLastName());
    }

    public boolean isValidPhone(String phone) {
        int phoneLength = phone.length();
        boolean isValidNumbers = phone.matches("^[0-9]+$");
        boolean isValidLength = phoneLength == 10;
        return isValidNumbers && isValidLength;
    }

    public boolean isValidNumbersAndLengthCustomerDTO(Map<String, String> requestMap){
        return isValidPhone(requestMap.get("phone"));
    }

    public boolean isValidNumbersAndLengthCustomerPlanDTO(CustomerPlanDTO customerPlanDTO){
        return isValidPhone(customerPlanDTO.getPhone());
    }

    public boolean isValidFoodLetters(String lettersField){
        if ( !lettersField.equals(null) || !lettersField.isEmpty()) {
            boolean isValidFieldLetters = lettersField.matches("^[\\p{L}\\p{M}]+$");
            return isValidFieldLetters;
        }
        return false;
    }
    public boolean isValidFoodNumbers(Double numbersField) {
        String numbersFieldAsString = String.valueOf(numbersField);
        boolean isValidFieldNumbers = numbersFieldAsString.matches("^[0-9,\\.]+$");
        return isValidFieldNumbers;
    }


    public boolean isValidFieldLetters(Map<String, String> requestMap){
        return isValidFoodLetters(requestMap.get("name"));
    }

    public boolean isValidFieldNumbers(Map<String, String> requestMap) {
        return isValidFoodNumbers(Double.valueOf(requestMap.get("gram"))) && isValidFoodNumbers(Double.valueOf(requestMap.get("calories")));
    }


}
