package com.example.plan.food.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.food.service.FoodService;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.utils.PlanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    @Override
    public Food findById(int id) {
        Optional<Food> optional = foodRepository.findById(id);
        Food food = null;
        if (optional.isPresent()){
           food = optional.get();
        } else {
            throw new RuntimeException("Ο φαγητό " + optional.get().getName() + " δεν βρέθηκε..");
        }
        return food;
    }

    @Override
    public List<Food> findByType(Type type) {
            List<Food> food = foodRepository.findByType(type);
            return food;
    }

    @Override
    public ResponseEntity<String> save(Food inputFood) {
        try {
            Food existingFood = foodRepository.findByName(inputFood.getName());
            if (Objects.isNull(existingFood)) {
                if (!inputFood.getMeals().isEmpty()) {
                    List<Meal> meals = inputFood.getMeals().stream()
                            .filter(meal -> meal.getId() == 0)
                            .map(meal -> mealRepository.save(meal))
                            .collect(Collectors.toList());
                    inputFood.setMeals(meals);
                }
                    foodRepository.save(inputFood);
            }else {
                return new ResponseEntity<>("To φαγητό " + "\"" + inputFood.getName() + "\"" + " υπάρχει ήδη..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("To φαγητό " + inputFood.getName() + " γράφτηκε επιτυχώς!", HttpStatus.CREATED);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Food inputFood, int id) {
        try {
            Optional<Food> existingFood = foodRepository.findById(id);
            if (existingFood.isPresent()){
                Food food = existingFood.get();
                food.setName(inputFood.getName());
                food.setDescription(inputFood.getDescription());
                food.setQuantity(inputFood.getQuantity());
                food.setGram(inputFood.getGram());
                food.setCalories(inputFood.getCalories());
                food.setType(inputFood.getType());
                foodRepository.save(food);
            } else {
                return new ResponseEntity<>("Το φαγητό ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Το φαγητό " + inputFood.getName() + " ενημερώθηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> delete(int id) {
        try {
            Optional<Food> food = foodRepository.findById(id);
            if (food.isPresent()) {
                foodRepository.deleteById(id);
            } else {
                return new ResponseEntity<>("Το φαγητό ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<> ("Το φαγητό "+ food.get().getName() + " διαγράφτηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
