package com.example.plan.food.service;

import com.example.plan.dto.food.FoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.utils.food.FoodResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FoodService {
    List<FoodDTO> findAll();

    FoodDTO findById(int id);

    List<FoodDTO> findByType (Type type);

    FoodDTO findByName(String name);

    ResponseEntity<FoodResponseMessage> saveFood(FoodDTO foodDTO);


    ResponseEntity<FoodResponseMessage> updateFood(FoodDTO foodDTO, int id);

    ResponseEntity<FoodResponseMessage> deleteFood(int id);
}
