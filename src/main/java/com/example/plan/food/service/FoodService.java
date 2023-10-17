package com.example.plan.food.service;

import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FoodService {
    List<Food> findAll();

    Food findById(int id);

    List<Food> findByType (Type type);

    ResponseEntity<String> save(Food inputFood);

    ResponseEntity<String> update(Food food, int id);

    ResponseEntity<String> delete(int id);
}
