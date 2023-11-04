package com.example.plan.meal.controller;

import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.meal.service.MealService;
import com.example.plan.utils.meal.MealResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<MealResponseMessage> saveMeal(@RequestBody MealDTO mealDTO) {
        return mealService.addMeal(mealDTO);
    }

    @PostMapping("/save/meal-foods")
    public ResponseEntity<MealResponseMessage> addMealWithFoods(@RequestBody MealFoodDTO mealFoodDTO) {
        return mealService.addMealWithFoods(mealFoodDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MealResponseMessage> updateMeal(@RequestBody MealDTO mealDTO, @PathVariable int id) {
        return mealService.updateMeal(mealDTO, id);
    }

    @PostMapping("/{id}/save/foods")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MealResponseMessage> addFoodToMeal(@RequestBody MealFoodDTO mealFoodDTO, @PathVariable int id) {
        return mealService.addFoodToMeal(mealFoodDTO, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MealResponseMessage> deleteMeal(@PathVariable int  id) {
        return mealService.deleteMeal(id);
    }

    @DeleteMapping("/delete/both/meal/{mealId}/food/{foodId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MealResponseMessage> deleteMealAndFood(@PathVariable int  mealId, @PathVariable int foodId) {
        return mealService.deleteMealAndFood(mealId, foodId);
    }

    @DeleteMapping("delete/{mealId}/food/{foodId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MealResponseMessage> removeFoodFromMeal(@PathVariable int  mealId, @PathVariable int foodId) {
        return mealService.removeFoodFromMeal(mealId, foodId);
    }
}
