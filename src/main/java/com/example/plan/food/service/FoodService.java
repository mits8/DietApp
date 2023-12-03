package com.example.plan.food.service;

import com.example.plan.enums.Type;
import com.example.plan.utils.ResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

@Transactional
public interface FoodService {
    List<Map<String, Object>> findAll();

    Map<String, Object> findById(int id);

    List<Map<String, Object>> findByType (Type type);

    Map<String, Object> findByName(String name);

    ResponseEntity<ResponseMessage> addFood(Map<String, Object> requestMap);

    ResponseEntity<ResponseMessage> updateFood(Map<String, Object> requestMap, int id);

    ResponseEntity<ResponseMessage> deleteFood(int id);
}
