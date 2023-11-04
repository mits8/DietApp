package com.example.plan.utils;

import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.plan.PlanDTO;
import com.example.plan.plan.entity.Plan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessage {

    private String message;

    private Plan plan;
    private PlanDTO PlanDTO;
    private CustomerDTO customerDTO;

    public ResponseMessage(String message, Plan plan) {
        this.message = message;
        this.plan = plan;
    }


    public ResponseMessage(String message, PlanDTO PlanDTO) {
        this.message = message;
        this.PlanDTO = PlanDTO;
    }

    public ResponseMessage(String message, CustomerDTO customerDTO) {
        this.message = message;
        this.customerDTO = customerDTO;
    }


}
