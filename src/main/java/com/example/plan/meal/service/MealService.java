package com.example.plan.meal.service;

import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.utils.meal.MealResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MealService {
    List<MealDTO> findAll();

    MealDTO findById(int id);

    List<MealDTO> findByTypeBreakfast(Type type);

    ResponseEntity<MealResponseMessage> addMeal(MealDTO mealDTO);

    @Transactional
    ResponseEntity<MealResponseMessage> addMealWithFoods(MealFoodDTO mealFoodDTO);

    @Transactional
    ResponseEntity<MealResponseMessage> addFoodToMeal(MealFoodDTO mealFoodDTO, int id);

    ResponseEntity<MealResponseMessage> updateMeal(MealDTO mealDTO, int id);

    ResponseEntity<MealResponseMessage> deleteMeal(int id);

    @Transactional
    ResponseEntity<MealResponseMessage> deleteMealAndFood(int mealId, int foodId);

    @Transactional
    ResponseEntity<MealResponseMessage> removeFoodFromMeal(int mealId, int foodId);
}
