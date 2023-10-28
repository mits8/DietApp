package com.example.plan.utils.customer;

import com.example.plan.dto.customer.CustomerDTO;
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
