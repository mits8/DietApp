package com.example.plan.meal.controller;

import com.example.plan.dto.food.FoodDTO;
import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.meal.service.MealService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meal")
public class MealController {


    @Autowired
    private MealService mealService;

    @GetMapping("/findAll")
    public ResponseEntity<List<MealDTO>> findAll(){
        return new ResponseEntity<>(mealService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MealDTO> findById(@PathVariable int id) {
        return new ResponseEntity<>(mealService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/type/breakfast")
    public ResponseEntity<List<MealDTO>> findByTypeBreakfast(@RequestParam("type") Type type) {
        return new ResponseEntity<>(mealService.findByTypeBreakfast(type), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> saveMeal(@RequestBody Map<String, String> requestMap) {
        return mealService.addMeal(requestMap);
    }

    @PostMapping("/save/meal-foods")
    public ResponseEntity<ResponseMessage> addMealWithFoods(@RequestBody MealFoodDTO mealFoodDTO) {
        return mealService.addMealWithFoods(mealFoodDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> updateMeal(@RequestBody Map<String, String> requestMap, @PathVariable int id) {
        return mealService.updateMeal(requestMap, id);
    }

    @PostMapping("/{id}/save/foods")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> addFoodToMeal(@RequestBody Map<String, List<FoodDTO>> requestMap, @PathVariable int id) {
        return mealService.addFoodToMeal(requestMap, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteMeal(@PathVariable int  id) {
        return mealService.deleteMeal(id);
    }

    @DeleteMapping("/delete/both/meal/{mealId}/food/{foodId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteMealAndFood(@PathVariable int  mealId, @PathVariable int foodId) {
        return mealService.deleteMealAndFood(mealId, foodId);
    }

    @DeleteMapping("delete/{mealId}/food/{foodId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> removeFoodFromMeal(@PathVariable int  mealId, @PathVariable int foodId) {
        return mealService.removeFoodFromMeal(mealId, foodId);
    }
}
