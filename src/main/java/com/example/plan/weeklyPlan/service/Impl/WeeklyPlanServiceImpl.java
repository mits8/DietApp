package com.example.plan.weeklyPlan.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.meal.dto.MealDTO;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.meal.service.MealService;
import com.example.plan.utils.PlanUtils;
import com.example.plan.weeklyPlan.dto.WeeklyPlanMealDTO;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import com.example.plan.weeklyPlan.repository.WeeklyPlanRepository;
import com.example.plan.weeklyPlan.service.WeeklyPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WeeklyPlanServiceImpl implements WeeklyPlanService {

    @Autowired
    private MealService mealService;
    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private WeeklyPlanRepository weeklyPlanRepository;

    @Override
    public List<WeeklyPlanMealDTO> findAll() {

        List<WeeklyPlan> weeklyPlans = weeklyPlanRepository.findAll();
        List<WeeklyPlanMealDTO> weeklyPlanMealDTOs = new ArrayList<>();


        for (WeeklyPlan weeklyPlan : weeklyPlans) {
            WeeklyPlanMealDTO dto = mapToWeeklyPlanMealDTO(weeklyPlan);
            weeklyPlanMealDTOs.add(dto);
        }

        return weeklyPlanMealDTOs;
    }

    private WeeklyPlanMealDTO mapToWeeklyPlanMealDTO(WeeklyPlan weeklyPlan) {
        WeeklyPlanMealDTO dto = new WeeklyPlanMealDTO();
        dto.setId(weeklyPlan.getId());
        dto.setWeeklyPlanName(weeklyPlan.getName());

        // You'll need to map the set of Meal entities to MealDTO objects
        Set<MealDTO> mealDTOs = weeklyPlan.getMeals().stream()
                .map(this::mapMealToMealDTO)
                .collect(Collectors.toSet());
        dto.setMeals(mealDTOs);

        return dto;
    }

    private MealDTO mapMealToMealDTO(Meal meal) {
        MealDTO mealDTO = new MealDTO();
        mealDTO.setId(meal.getId());
        mealDTO.setName(meal.getName());
        mealDTO.setDescription(meal.getDescription());
        mealDTO.setType(meal.getType());

        // You can map any associated entities (e.g., foods) to DTOs here

        return mealDTO;
    }

    @Override
    public ResponseEntity<String> save(WeeklyPlan inputWeeklyPlan) {
        try {
            WeeklyPlan existingPlan = weeklyPlanRepository.findByName(inputWeeklyPlan.getName());

            if (Objects.isNull(existingPlan)) {

                Set<Meal> mealsToSave = inputWeeklyPlan.getMeals().stream()
                        .filter(meal -> meal.getId() == 0)
                        .map(meal -> mealRepository.save(meal))
                        .collect(Collectors.toSet());

                inputWeeklyPlan.setMeals(mealsToSave);
                weeklyPlanRepository.save(inputWeeklyPlan);

                return new ResponseEntity<>("Το πλάνο " + "\"" + inputWeeklyPlan.getName() + "\"" + " γράφτηκε επιτυχώς!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Το πλάνο " + "\"" + existingPlan.getName() + "\"" + " υπάρχει ήδη..", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error while saving WeeklyPlan: {}", ex);
            return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}