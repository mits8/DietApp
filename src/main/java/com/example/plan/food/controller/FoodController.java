package com.example.plan.food.controller;

import com.example.plan.food.entity.Food;
import com.example.plan.food.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Food>> findAll(){
        return new ResponseEntity<>(foodService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Food> findById(@PathVariable int id) {
        return new ResponseEntity<>(foodService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> save(@RequestBody Food food) {
        return foodService.save(food);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateCustomer(@RequestBody Food food, @PathVariable int id) {
        return foodService.update(food, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCustomer(@PathVariable int  id) {
        return foodService.delete(id);
    }
}
