package com.example.plan.meal.controller;

import com.example.plan.customerInfo.entity.CustomerInfo;
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
    public ResponseEntity<List<Map<String, Object>>> findAll(){
        return new ResponseEntity<>(mealService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable int id) {
        return new ResponseEntity<>(mealService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<Map<String, Object>>> findByType(@RequestParam("type") Type type) {
        return new ResponseEntity<>(mealService.findByType(type), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addMeal(@RequestBody Map<String, Object> requestMap) {
        return mealService.addMeal(requestMap);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> updateMeal(@RequestBody Map<String, Object> requestMap, @PathVariable int id) {
        return mealService.updateMeal(requestMap, id);
    }

    @PostMapping("/save/foods/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> addFoodToMeal(@RequestBody Map<String, List<Object>> requestMap,@PathVariable String name) {
        return mealService.addFoodToMeal(requestMap, name);
    }

    @GetMapping("/recommendMeals/customerId{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> recommendMeals(@PathVariable Long id, @RequestParam("type") Type type, @RequestBody Map<String, Object> requestMap) {
        return mealService.recommendMeals(id, type, requestMap);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteMeal(@PathVariable int  id) {
        return mealService.deleteMeal(id);
    }

}
