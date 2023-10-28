package com.example.plan.utils.customer;

import com.example.plan.dto.customer.CustomerWeeklyPlanDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerWeeklyPlanResponseMessage {

    private String message;
    private CustomerWeeklyPlanDTO customerWeeklyPlanDTO;

    public CustomerWeeklyPlanResponseMessage(String message, CustomerWeeklyPlanDTO customerWeeklyPlanDTO) {
        this.message = message;
        this.customerWeeklyPlanDTO = customerWeeklyPlanDTO;
    }
}
