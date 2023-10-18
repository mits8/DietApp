package com.example.plan.meal.controller;

import com.example.plan.enums.Type;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.service.MealService;
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
    public ResponseEntity<List<Meal>> findAll(){
        return new ResponseEntity<>(mealService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Meal> findById(@PathVariable int id) {
        return new ResponseEntity<>(mealService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/type/breakfast")
    public ResponseEntity<List<Meal>> findByTypeBreakfast(Type type) {
        return new ResponseEntity<>(mealService.findByTypeBreakfast(type), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Meal meal) {
        return mealService.save(meal);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateCustomer(@RequestBody Meal meal, @PathVariable int id) {
        return mealService.update(meal, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCustomer(@PathVariable int  id) {
        return mealService.delete(id);
    }
}
