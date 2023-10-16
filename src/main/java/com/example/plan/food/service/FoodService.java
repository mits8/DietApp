package com.example.plan.food.service;

import com.example.plan.food.entity.Food;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FoodService {
    List<Food> findAll();

    Food findById(int id);

    ResponseEntity<String> save(Food inputFood);

    ResponseEntity<String> update(Food food, int id);

    ResponseEntity<String> delete(int id);
}
