package com.example.plan.weeklyPlan.service;

import com.example.plan.weeklyPlan.dto.WeeklyPlanMealDTO;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WeeklyPlanService {
    List<WeeklyPlanMealDTO> findAll();

    ResponseEntity<String> save(WeeklyPlan inputWeeklyPlan);
}
