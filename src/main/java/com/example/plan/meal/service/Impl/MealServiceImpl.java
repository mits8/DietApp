package com.example.plan.meal.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.enums.Type;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.meal.service.MealService;
import com.example.plan.utils.PlanUtils;
import com.example.plan.weeklyPlan.repository.WeeklyPlanRepository;
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
public class MealServiceImpl implements MealService {
    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private WeeklyPlanRepository weeklyPlanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Meal> findAll() {
        return mealRepository.findAll();
    }

    @Override
    public Meal findById(int id) {
        Optional<Meal> optional = mealRepository.findById(id);
        Meal meal = null;
        if (optional.isPresent()){
            meal = optional.get();
        } else {
            throw new RuntimeException("Το γεύμα " + "\"" + optional.get().getName() + "\"" + " δεν βρέθηκε..");
        }
        return meal;
    }

    @Override
    public List<Meal> findByTypeBreakfast(Type type) {
        type = Type.BREAKFAST;
        List<Meal> meal = mealRepository.findByType(type);
        return meal;
    }

    @Override
    public ResponseEntity<String> save(Meal inputMeal) {
        try {
            Meal meal = mealRepository.findByName(inputMeal.getName());
            if (Objects.isNull(meal)) {
                mealRepository.save(inputMeal);
            }else {
                return new ResponseEntity<>("Το γεύμα " + "\"" + inputMeal.getName() + "\"" + " υπάρχει ήδη..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Το γεύμα " + "\"" + inputMeal.getName() + "\"" + " γράφτηκε επιτυχώς!", HttpStatus.CREATED);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Meal meal, int id) {
        try {
            Optional<Meal> existingMeal =  mealRepository.findById(id);
            if (existingMeal.isPresent()){
                Meal updateMeal = existingMeal.get();
                updateMeal.setName(meal.getName());
                updateMeal.setDescription(meal.getDescription());
                updateMeal.setType(meal.getType());
                mealRepository.save(updateMeal);
            } else {
                return new ResponseEntity<>("Το γεύμα ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Το γεύμα " + "\"" + existingMeal.get().getName() + "\"" + " ενημερώθηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> delete(int id) {
        try {
            Optional<Meal> meal =  mealRepository.findById(id);
            if (meal.isPresent()){
                mealRepository.deleteById(id);
            } else {
                return new ResponseEntity<>("Το γεύμα ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<> ("Το γεύμα " + "\"" + meal.get().getName() + "\"" + " διαγράφτηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
