package com.example.plan.plan.service;

import com.example.plan.utils.ResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PlanService {
    List<Map<String, Object>> findAll();

    List<Map<String, Object>> findPlanMealFood();

    List<Map<String, Object>> getPlanDetailsByCustomerFullName(String firstname, String surname, LocalDate birthdate);

    ResponseEntity<ResponseMessage> count();

    ResponseEntity<ResponseMessage> generateReport(Map<String, Object> requestMap, String firstname, String surname, LocalDate startDate, LocalDate endDate);

    @Transactional
    ResponseEntity<ResponseMessage> addPlan(Map<String, Object> requestMap);

    @Transactional
    ResponseEntity<ResponseMessage> addToPlan(Map<String, List<Object>> requestMap, int id);

    @Transactional
    ResponseEntity<ResponseMessage> addMealToPlan(Map<String, List<Object>> mealData, String name, LocalDate startDate, LocalDate endDate);

    @Transactional
    ResponseEntity<ResponseMessage> addCustomerToPlan(Map<String, List<Object>> requestMap, String name);

    @Transactional
    ResponseEntity<ResponseMessage> updatePlan(Map<String, Object> requestMap, int id);

    @Transactional
    ResponseEntity<ResponseMessage> deletePlan(int id);

    @Transactional
    ResponseEntity<ResponseMessage> removeCustomerFromPlan(Long customerId, int PlanId);

    @Transactional
    ResponseEntity<ResponseMessage> removeFoodFromMeal(int planId, int mealId, int foodId);

    @Transactional
    ResponseEntity<ResponseMessage> deleteMealAndFood(int planId, int mealId, int foodId);

    @Transactional
    ResponseEntity<ResponseMessage> deleteAll();
}
