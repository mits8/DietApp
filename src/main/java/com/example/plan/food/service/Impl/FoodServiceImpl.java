package com.example.plan.food.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.food.service.FoodService;
import com.example.plan.utils.PlanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CustomerRepository customerRepository;

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
            Food food = foodRepository.findByName(inputFood.getName());
            if (Objects.isNull(food)) {
                    inputFood.setName(inputFood.getName());
                    inputFood.setGram(inputFood.getGram());
                    inputFood.setCalories(inputFood.getCalories());
                    inputFood.setType(inputFood.getType());
                    foodRepository.save(inputFood);
            }else {
                return new ResponseEntity<>("To φαγητό " + "\"" + food.getName() + "\"" + " υπάρχει ήδη..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("To φαγητό " + inputFood.getName() + " γράφτηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Food food, int id) {
        try {
            Optional<Food> existingFood = foodRepository.findById(id);
            if (existingFood.isPresent()){
                Food updateFood = existingFood.get();
                updateFood.setName(food.getName());
                updateFood.setGram(food.getGram());
                updateFood.setCalories(food.getCalories());
                foodRepository.save(updateFood);
            } else {
                return new ResponseEntity<>("Το φαγητό ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Το φαγητό " + food.getName() + " ενημερώθηκε επιτυχώς!", HttpStatus.OK);
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
