package com.example.plan.meal.service.Impl;

import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customerInfo.entity.CustomerInfo;
import com.example.plan.customerInfo.repository.CustomerInfoRepository;
import com.example.plan.enums.Day;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.meal.service.MealService;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional
public class MealServiceImpl implements MealService {
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerInfoRepository customerInfoRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Map<String, Object>> findAll() {
        List<Meal> meals = mealRepository.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Meal meal : meals) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", meal.getId());
            map.put("name", meal.getName());
            map.put("description", meal.getDescription());
            map.put("type", meal.getType());
            map.put("day", meal.getDay());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public Map<String, Object> findById(int id) {
        Optional<Meal> existingMeal = mealRepository.findById(id);
        if (existingMeal.isPresent()) {
            Meal meal = existingMeal.get();
            Map<String, Object> map = new HashMap<>();
            map.put("id", meal.getId());
            map.put("name", meal.getName());
            map.put("description", meal.getDescription());
            map.put("type", meal.getType());
            map.put("day", meal.getDay());

            return map;
        } else {
            throw new RuntimeException("Το γεύμα " + "'" + existingMeal.get().getName() + "'" + " δεν βρέθηκε..");
        }
    }

    @Override
    public List<Map<String, Object>> findByType(Type type) {
        List<Meal> meals = mealRepository.findByType(type);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Meal meal : meals) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", meal.getId());
            map.put("name", meal.getName());
            map.put("description", meal.getDescription());
            map.put("type", meal.getType());
            map.put("day", meal.getDay());
            mapList.add(map);
        }
        return mapList;
    }
    public ResponseEntity<ResponseMessage> addMeal(Map<String, Object> requestMap) {
        try {
            String mealName = (String) requestMap.get("name");
            Meal existingMeal = mealRepository.findMealName(mealName);

            if (existingMeal == null) {
                Meal meal = new Meal();
                meal.setName(mealName);
                meal.setDescription((String) requestMap.get("description"));
                meal.setDay(Day.valueOf(String.valueOf(requestMap.get("day"))));
                meal.setType(Type.valueOf(String.valueOf(requestMap.get("type"))));

                mealRepository.save(meal);
                String message = "Το γεύμα '" + mealName + "' γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            String message = "Το γεύμα '" + mealName + "' υπάρχει ήδη.";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος.";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<ResponseMessage> addFoodToMeal(Map<String, List<Object>> requestMap, String nameOfMeal) {
        try {
            Meal existingMeal = mealRepository.findMealName(nameOfMeal);
            Meal meal;
            if (existingMeal != null) {
                meal = existingMeal;
                addFoods(meal, requestMap);

                String message = "Το γεύμα '" + requestMap.get("name") + "' ενημερώθηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                List<Object> meals = requestMap.get("meals");
                for (Object mealObject : meals) {

                    Map<String, Object> map = objectMapper.convertValue(mealObject, Map.class);
                    String name = (String) map.get("name");
                    String description = (String) map.get("description");
                    Day day = Day.valueOf(String.valueOf(map.get("day")));
                    Type type = Type.valueOf(String.valueOf(map.get("type")));

                    meal = new Meal();
                    meal.setName(name);
                    meal.setDescription(description);
                    meal.setType(type);
                    meal.setDay(day);
                    mealRepository.save(meal);
                    addFoods(meal, requestMap);

                    String message = "Το γεύμα '" + map.get("name") + "' ενημερώθηκε επιτυχώς!";
                    ResponseMessage response = new ResponseMessage(message, null);
                    return new ResponseEntity<>(response, HttpStatus.CREATED);
                }
            }
            String message = "Το γεύμα ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }

        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private void addFoods(Meal meal, Map<String, List<Object>> requestMap) {
        List<Object> foods = requestMap.get("foods");
        if (foods != null) {
            for (Object foodObject : foods) {
                Map<String, String> map = objectMapper.convertValue(foodObject, Map.class);

                String foodName = String.valueOf(map.get("name"));
                String foodDescr = String.valueOf(map.get("description"));
                String foodQuantiity = String.valueOf(map.get("quantity"));
                Double calories = Double.valueOf(String.valueOf(map.get("calories")));
                Type foodType = Type.valueOf(map.get("type"));
                Food existingFood = foodRepository.findFoodName(foodName);
                if (existingFood == null) {
                    existingFood = new Food();
                    existingFood.setName(foodName);
                    existingFood.setDescription(foodDescr);
                    existingFood.setQuantity(foodQuantiity);
                    existingFood.setCalories(calories);
                    existingFood.setType(foodType);
                    foodRepository.save(existingFood);
                }
                boolean relationshipExists = mealRepository.existsMealFoodRelationship(meal, existingFood);

                if (!relationshipExists) {
                    meal.getFoods().add(existingFood);
                }
            }
        }
        mealRepository.save(meal);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateMeal(Map<String, Object> requestMap, int id) {
        try {
            Optional<Meal> existingMeal =  mealRepository.findById(id);
            if (existingMeal.isPresent()){
                Meal updateMeal = existingMeal.get();
                updateMeal.setName((String) requestMap.get("name"));
                updateMeal.setDescription((String) requestMap.get("description"));
                updateMeal.setDay(Day.valueOf(String.valueOf(requestMap.get("day"))));
                updateMeal.setType(Type.valueOf(String.valueOf(requestMap.get("type"))));
                mealRepository.save(updateMeal);
                String message = "Το γεύμα " + "'" + requestMap.get("name") + " ενημερώθηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response , HttpStatus.OK);
            }
            String message = "Το γεύμα ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, requestMap);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> recommendMeals(Long id, Type type, Map<String, Object> requestMap) {
        try {
            Optional<DietCustomer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                DietCustomer customer = optionalCustomer.get();
                List<CustomerInfo> customerInfos = customerInfoRepository.findLatestCustomerInfoByCustomerId(customer.getId());

                Optional<LocalDateTime> mostRecentMeasureDate = customerInfos.stream()
                        .map(CustomerInfo::getCreatedDate)
                        .max(LocalDateTime::compareTo);

                LocalDateTime recentlyMeasured;
                CustomerInfo customerInfo = null;
                if (mostRecentMeasureDate.isPresent()) {
                    recentlyMeasured = mostRecentMeasureDate.get();
                    customerInfo = customerInfoRepository.findLatestCustomerInfo(recentlyMeasured);
                }

                double requirements = Utils.calculateTotalCalories(customerInfo);

                List<Meal> recommendedMeals = null;
                if (requirements < 1000) {
                    recommendedMeals = mealRepository.findMealsByCaloriesLessThanEqual(Utils.getDouble(requestMap, "minCalories"), type);
                } else if (requirements > 1000 && requirements < 3000) {
                    recommendedMeals = mealRepository.findMealsByCaloriesBetweenEqual(Utils.getDouble(requestMap, "minCalories"), Utils.getDouble(requestMap, "maxCalories"), type);
                } else if (requirements > 5000){
                    recommendedMeals = mealRepository.findMealsByCaloriesMoreThanEqual(Utils.getDouble(requestMap, "maxCalories"), type);
                }
                List<Map<String, Object>> mapList = new ArrayList<>();

                if (recommendedMeals != null || !recommendedMeals.isEmpty()) {
                    for (Meal meal : recommendedMeals) {
                        Map<String, Object> mealMap = new HashMap<>();
                        mealMap.put("id", meal.getId());
                        mealMap.put("name", meal.getName());
                        mealMap.put("type", meal.getType());
                        mealMap.put("calories", meal.getCalories());
                        mapList.add(mealMap);
                    }
                }
                String message = "Recommended meals found successfully!";
                ResponseMessage response = new ResponseMessage(message, mapList);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                String message = "No recommended meals found for the given criteria.";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Error recommending meals: {}", ex.getMessage());
            String message = "An error occurred while recommending meals.";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteMeal(int id) {
        try {
            Optional<Meal> optionalMeal =  mealRepository.findById(id);
            if (optionalMeal.isPresent()){
                mealRepository.delete(optionalMeal.get());
                String message = "Το γεύμα " + "'" + optionalMeal.get().getName() + " διαγράφηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            }
            String message = "Το γεύμα ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("An error occurred while deleting the meal with ID " + id, ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
