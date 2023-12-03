package com.example.plan.food.service.Impl;

import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.food.service.FoodService;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.validation.Validation;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@Transactional
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private Validation validation;

    List<Map<String, Object>> mapList = new ArrayList<>();
    Map<String, Object> map = new HashMap<>();

    @Override
    public List<Map<String, Object>> findAll() {
        List<Food> foods = foodRepository.findAll();
        for(Food food : foods) {
            map.put("id", food.getId());
            map.put("name", food.getName());
            map.put("description", food.getDescription());
            map.put("quantity", food.getQuantity());
            map.put("calories", food.getCalories());
            map.put("type", food.getType());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public Map<String, Object> findById(int id) {
        Optional<Food> existingFood = foodRepository.findById(id);
        if (existingFood.isPresent()){
          Food food = existingFood.get();
            map.put("id", food.getId());
            map.put("name", food.getName());
            map.put("description", food.getDescription());
            map.put("quantity", food.getQuantity());
            map.put("calories", food.getCalories());
            map.put("type", food.getType());

        return map;
        } else {
            throw new RuntimeException("Το φαγητό δεν βρέθηκε..");
        }
    }

    @Override
    public List<Map<String, Object>> findByType(Type type) {
        List<Food> foods = foodRepository.findByType(type);
        for (Food food : foods) {
            map.put("id", food.getId());
            map.put("name", food.getName());
            map.put("description", food.getDescription());
            map.put("quantity", food.getQuantity());
            map.put("calories", food.getCalories());
            map.put("type", food.getType());
            mapList.add(map);
        }
        return mapList;
    }

    /*------------------REVIEW----------------*/
    @Override
    public Map<String, Object> findByName(String name) {

        Food food = foodRepository.findByName(name);
            map.put("id", food.getId());
            map.put("name", food.getName());
            map.put("description", food.getDescription());
            map.put("quantity", food.getQuantity());
            map.put("calories", food.getCalories());
            map.put("type", food.getType());

            return map;
    }

    @Override
    public ResponseEntity<ResponseMessage> addFood(Map<String, Object> requestMap) {
        try {
            Food foodName = foodRepository.findFoodName((String) requestMap.get("name"));
            if (foodName == null) {
                if (validation.isValidFieldLetters(requestMap) && validation.isValidFieldNumbers(requestMap)) {
                    Food food = new Food();
                    food.setName((String) requestMap.get("name"));
                    food.setDescription((String) requestMap.get("description"));
                    food.setQuantity((String) requestMap.get("quantity"));
                    food.setCalories(Double.parseDouble((String.valueOf(requestMap.get("calories")))));
                    food.setType(Type.valueOf((String.valueOf(requestMap.get("type")))));
                    foodRepository.save(food);
                    String message = "Το φαγητό " + "'" + requestMap.get("name") + "'" + " γράφτηκε επιτυχώς!";
                    ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    String message = "Κάποιο πεδίο είναι λάθος..";
                    ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }
            String message = "Το φαγητό " + "'" + requestMap.get("name") + "'" + " υπάρχει ήδη..";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, requestMap);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateFood(Map<String, Object> requestMap, int id) {
        try {
            Optional<Food> existingFood = foodRepository.findById(id);
            if (existingFood.isPresent()){
                Food updateFood = existingFood.get();
                if (validation.isValidFieldLetters(requestMap) && validation.isValidFieldNumbers(requestMap)) {
                        updateFood.setName((String) (requestMap.get("name")));
                        updateFood.setDescription((String) requestMap.get("description"));
                        updateFood.setQuantity(String.valueOf( requestMap.get("quantity")));
                        updateFood.setCalories(Double.parseDouble((String.valueOf(requestMap.get("calories")))));
                        updateFood.setType(Type.valueOf((String.valueOf(requestMap.get("type")))));
                        foodRepository.save(updateFood);
                        String message = "Το φαγητό " + "'" + requestMap.get("name") + "'" + " ενημερώθηκε επιτυχώς!";
                        ResponseMessage response = new ResponseMessage(message, requestMap);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    String message = "Κάποιο πεδίο είναι λάθος";
                        ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }
            String message = "Το φαγητό ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, requestMap);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteFood(int id) {
        try {
            Optional<Food> food = foodRepository.findById(id);
            if (food.isPresent()) {
                foodRepository.deleteById(id);
                String message = "Το φαγητό "+ food.get().getName() + " διαγράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "Το φαγητό ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
