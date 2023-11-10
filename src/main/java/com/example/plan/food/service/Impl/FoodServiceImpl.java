package com.example.plan.food.service.Impl;

import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.dto.food.FoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.food.service.FoodService;
import com.example.plan.map.Mapper;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.food.FoodResponseMessage;
import com.example.plan.validation.Validation;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private Validation validation;

    @Override
    public List<FoodDTO> findAll() {
        List<Food> foods = foodRepository.findAll();
        List<FoodDTO> foodDTOS = foods.stream()
                .map(mapper::mapFoodToFoodDTO)
                .collect(Collectors.toList());
        return foodDTOS;
    }

    @Override
    public FoodDTO findById(int id) {
        Optional<Food> existingFood = foodRepository.findById(id);
        Food food = null;
        if (existingFood.isPresent()){
           food = existingFood.get();
           FoodDTO foodDTO = mapper.mapFoodToFoodDTO(food);
        return foodDTO;
        } else {
            throw new RuntimeException("Το φαγητό δεν βρέθηκε..");
        }
    }

    @Override
    public List<FoodDTO> findByType(Type type) {
            List<Food> foods = foodRepository.findByType(type);
            List<FoodDTO> foodDTOS = foods.stream()
                    .map(food -> mapper.mapFoodToFoodDTO(food))
                    .collect(Collectors.toList());
            return foodDTOS;
    }

    @Override
    public FoodDTO findByName(String name) {
        Food food = foodRepository.findByName(name);
        FoodDTO foodDTO = mapper.mapFoodToFoodDTO(food);
        return foodDTO;
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addFood(Map<String, String> requestMap) {
        try {
            Food name = foodRepository.findByName(requestMap.get("name"));
            if (Objects.isNull(name)) {
                if (validation.isValidFieldLetters(requestMap) && validation.isValidFieldNumbers(requestMap)) {
                    Food food = new Food();
                    food.setName(requestMap.get("name"));
                    food.setDescription(requestMap.get("description"));
                    food.setGram(Double.parseDouble(requestMap.get("gram")));
                    food.setCalories(Double.parseDouble(requestMap.get("calories")));
                    food.setType(Type.valueOf(requestMap.get("type")));
                    foodRepository.save(food);
                    String message = "Το φαγητό " + "'" + requestMap.get("name") + "'" + " γράφτηκε επιτυχώς!";
                    ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    String message = "Κάποιο πεδίο είναι λάθος..";
                    ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }else {
                String message = "Το φαγητό " + "'" + requestMap.get("name") + "'" + " υπάρχει ήδη..";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, requestMap);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> updateFood(Map<String, String> requestMap, int id) {
        try {
            Optional<Food> existingFood = foodRepository.findById(id);
            if (existingFood.isPresent()){
                Food updateFood = existingFood.get();
                if (validation.isValidFieldLetters(requestMap) && validation.isValidFieldNumbers(requestMap)) {
                        updateFood.setName(requestMap.get("name"));
                        updateFood.setDescription(requestMap.get("description"));
                        updateFood.setGram(Double.parseDouble(requestMap.get("gram")));
                        updateFood.setCalories(Double.parseDouble(requestMap.get("calories")));
                        updateFood.setType(Type.valueOf(requestMap.get("type")));
                        foodRepository.save(updateFood);
                        String message = "Το φαγητό " + "'" + requestMap.get("name") + "'" + " ενημερώθηκε επιτυχώς!";
                        ResponseMessage response = new ResponseMessage(message, requestMap);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    String message = "Κάποιο πεδίο είναι λάθος";
                        ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            } else {
                String message = "Το φαγητό ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, requestMap);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<FoodResponseMessage> deleteFood(int id) {
        try {
            Optional<Food> food = foodRepository.findById(id);
            if (food.isPresent()) {
                foodRepository.deleteById(id);
                String message = "Το φαγητό "+ food.get().getName() + " διαγράφτηκε επιτυχώς!";
                FoodResponseMessage response = new FoodResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                String message = "Το φαγητό ΔΕΝ βρέθηκε..";
                FoodResponseMessage response = new FoodResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        FoodResponseMessage response = new FoodResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
