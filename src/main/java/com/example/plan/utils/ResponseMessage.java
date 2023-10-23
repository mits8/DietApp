package com.example.plan.utils;

import com.example.plan.dto.CustomerDTO;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessage {

    private String message;
    private WeeklyPlan weeklyPlan;
    private CustomerDTO customerDTO;

    public ResponseMessage(String message, WeeklyPlan weeklyPlan) {
        this.message = message;
        this.weeklyPlan = weeklyPlan;
    }

    public ResponseMessage(String message, CustomerDTO customerDTO) {
        this.message = message;
        this.customerDTO = customerDTO;
    }


}
