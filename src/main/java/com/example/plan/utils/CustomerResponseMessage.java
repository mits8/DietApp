package com.example.plan.utils;

import com.example.plan.dto.CustomerDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseMessage {

    private String message;
    private CustomerDTO customerDTO;

    public CustomerResponseMessage(String message, CustomerDTO customerDTO) {
        this.message = message;
        this.customerDTO = customerDTO;
    }
}
