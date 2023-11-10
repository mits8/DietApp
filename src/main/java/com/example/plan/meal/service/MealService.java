package com.example.plan.meal.service;

import com.example.plan.dto.food.FoodDTO;
import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.meal.MealResponseMessage;
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
    ResponseEntity<MealResponseMessage> addMealWithFoods(MealFoodDTO mealFoodDTO);

    @Transactional
    ResponseEntity<ResponseMessage> addFoodToMeal(Map<String, List<FoodDTO>> requestMap, int id);

    ResponseEntity<ResponseMessage> updateMeal(Map<String, String> requestMap, int id);

    ResponseEntity<MealResponseMessage> deleteMeal(int id);

    @Transactional
    ResponseEntity<MealResponseMessage> deleteMealAndFood(int mealId, int foodId);

    @Transactional
    ResponseEntity<MealResponseMessage> removeFoodFromMeal(int mealId, int foodId);
}
