package com.example.plan.weeklyPlan.service;

import com.example.plan.utils.ResponseMessageWithEntity;
import com.example.plan.dto.WeeklyPlanMealCustomerDTO;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WeeklyPlanService {
    List<WeeklyPlanMealCustomerDTO> findAll();

    ResponseEntity<ResponseMessageWithEntity> save(WeeklyPlan inputWeeklyPlan);
}
