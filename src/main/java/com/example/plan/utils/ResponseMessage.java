package com.example.plan.utils;

import com.example.plan.dto.Plan.PlanDTO;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.plan.entity.Plan;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ResponseMessage {

    private String message;

    private Plan plan;
    private PlanDTO planDTO;
    private CustomerDTO customerDTO;

    private MealFoodDTO mealFoodDTO;

    private Map<String, String> requestMap;


    public ResponseMessage(String message, Map<String, String> requestMap) {
        this.message = message;
        this.requestMap = requestMap;
    }


    public ResponseMessage(String message, Plan plan) {
        this.message = message;
        this.plan = plan;
    }


    public ResponseMessage(String message, PlanDTO planDTO) {
        this.message = message;
        this.planDTO = planDTO;
    }

    public ResponseMessage(String message, CustomerDTO customerDTO) {
        this.message = message;
        this.customerDTO = customerDTO;
    }

    public ResponseMessage(String message, MealFoodDTO mealFoodDTO) {
        this.message = message;
        this.mealFoodDTO = mealFoodDTO;
    }
}
