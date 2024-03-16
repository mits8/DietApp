package com.example.plan.meal.service;

import com.example.plan.enums.Type;
import com.example.plan.utils.ResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Transactional
public interface MealService {
    List<Map<String, Object>> findAll();

    Map<String, Object> findById(int id);

    List<Map<String, Object>> findByType(Type type);

    ResponseEntity<ResponseMessage> addMeal(Map<String, Object> requestMap);

    ResponseEntity<ResponseMessage> addFoodToMeal(Map<String, List<Object>> requestMap, String name);

    ResponseEntity<ResponseMessage> updateMeal(Map<String, Object> requestMap, int id);

    ResponseEntity<ResponseMessage> recommendMeals(Long id, Type type, Map<String, Object> requestMap);

    ResponseEntity<ResponseMessage> deleteMeal(int id);

}
