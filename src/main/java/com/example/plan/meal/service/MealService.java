package com.example.plan.meal.service;

import com.example.plan.enums.Type;
import com.example.plan.meal.entity.Meal;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MealService {
    List<Meal> findAll();

    Meal findById(int id);

    List<Meal> findByTypeBreakfast(Type type);

    ResponseEntity<String> save(Meal meal);

    ResponseEntity<String> update(Meal meal, int id);

    ResponseEntity<String> delete(int id);
}
