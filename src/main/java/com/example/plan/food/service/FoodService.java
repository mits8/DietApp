package com.example.plan.food.service;

import com.example.plan.dto.food.FoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.utils.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FoodService {
    List<FoodDTO> findAll();

    FoodDTO findById(int id);

    List<FoodDTO> findByType (Type type);

    FoodDTO findByName(String name);

    ResponseEntity<ResponseMessage> addFood(Map<String, String> requestMap);


    ResponseEntity<ResponseMessage> updateFood(Map<String, String> requestMap, int id);

    ResponseEntity<ResponseMessage> deleteFood(int id);
}
