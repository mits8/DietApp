package com.example.plan.weeklyPlan.service;

import com.example.plan.dto.weeklyPlan.WeeklyPlanDTO;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.dto.weeklyPlan.WeeklyPlanMealCustomerDTO;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WeeklyPlanService {
    List<WeeklyPlanMealCustomerDTO> findAll();

    ResponseEntity<ResponseMessage> addWeeklyPlan(WeeklyPlan inputWeeklyPlan);

    @Transactional
    ResponseEntity<ResponseMessage> updateWeeklyPlan(WeeklyPlanDTO weeklyPlanDTO, int id);

    @Transactional
    ResponseEntity<ResponseMessage> deleteWeeklyPlan(int id);

    @Transactional
    ResponseEntity<CustomerResponseMessage> removeCustomerFromWeeklyPlan(int customerId, int weeklyPlanId);
}
