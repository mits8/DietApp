package com.example.plan.utils.customer;

import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerWeeklyPlanDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseMessage {

    private String message;
    private CustomerDTO customerDTO;

    private CustomerWeeklyPlanDTO customerWeeklyPlanDTO;


    public CustomerResponseMessage(String message, CustomerDTO customerDTO) {
        this.message = message;
        this.customerDTO = customerDTO;
    }

    public CustomerResponseMessage(String message, CustomerWeeklyPlanDTO customerWeeklyPlanDTO) {
        this.message = message;
        this.customerWeeklyPlanDTO = customerWeeklyPlanDTO;
    }
}
