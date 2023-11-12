package com.example.plan.meal.service;

import com.example.plan.dto.food.FoodDTO;
import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.utils.ResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MealService {
    List<MealDTO> findAll();

    MealDTO findById(int id);

    List<MealDTO> findByTypeBreakfast(Type type);

    ResponseEntity<ResponseMessage> addMeal(Map<String, String> requestMap);

    @Transactional
    ResponseEntity<ResponseMessage> addMealWithFoods(MealFoodDTO mealFoodDTO);

    @Transactional
    ResponseEntity<ResponseMessage> addFoodToMeal(Map<String, List<FoodDTO>> requestMap, int id);

    ResponseEntity<ResponseMessage> updateMeal(Map<String, String> requestMap, int id);

    ResponseEntity<ResponseMessage> deleteMeal(int id);

    @Transactional
    ResponseEntity<ResponseMessage> deleteMealAndFood(int mealId, int foodId);

    @Transactional
    ResponseEntity<ResponseMessage> removeFoodFromMeal(int mealId, int foodId);
}
