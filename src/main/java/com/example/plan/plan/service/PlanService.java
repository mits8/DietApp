package com.example.plan.plan.service;

import com.example.plan.dto.plan.PlanMealCustomerDTO;
import com.example.plan.utils.ResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface PlanService {
    List<PlanMealCustomerDTO> findAll();

    List<Object> getPlanDetailsByCustomerFirstName(String customerFirstName);

    @Transactional
    ResponseEntity<ResponseMessage> addToPlan(Map<String, List<Object>> requestMap, int id);

    @Transactional
    ResponseEntity<ResponseMessage> addMealToPlan(Map<String, List<Object>> mealData, int id);

    @Transactional
    ResponseEntity<ResponseMessage> addCustomerToPlan(Map<String, List<Object>> requestMap, int id);

    @Transactional
    ResponseEntity<ResponseMessage> updatePlan(Map<String, String> requestMap, int id);

    @Transactional
    ResponseEntity<ResponseMessage> deletePlan(int id);

    @Transactional
    ResponseEntity<ResponseMessage> removeCustomerFromPlan(int customerId, int PlanId);
}
