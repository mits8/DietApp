package com.example.plan.utils;

import com.example.plan.dto.CustomerWeeklyPlanDTO;
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
