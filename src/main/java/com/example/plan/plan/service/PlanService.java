package com.example.plan.plan.service;

import com.example.plan.dto.Plan.PlanDTO;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.plan.PlanMealCustomerDTO;
import com.example.plan.meal.entity.Meal;
import com.example.plan.plan.entity.Plan;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.customer.CustomerResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface PlanService {
    List<PlanMealCustomerDTO> findAll();

    ResponseEntity<ResponseMessage> addPlan(Plan inputPlan);

    @Transactional
    ResponseEntity<ResponseMessage> addMealToPlan(Map<String, List<MealDTO>> mealData, int id);

    @Transactional
    ResponseEntity<ResponseMessage> addCustomerToPlan(Map<String, List<CustomerDTO>> mealData, int id);

    @Transactional
    ResponseEntity<ResponseMessage> updatePlan(PlanDTO planDTO, int id);

    @Transactional
    ResponseEntity<ResponseMessage> deletePlan(int id);

    @Transactional
    ResponseEntity<CustomerResponseMessage> removeCustomerFromPlan(int customerId, int PlanId);
}
