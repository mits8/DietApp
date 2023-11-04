package com.example.plan.plan.service;

import com.example.plan.dto.plan.PlanDTO;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.dto.plan.PlanMealCustomerDTO;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.plan.entity.Plan;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlanService {
    List<PlanMealCustomerDTO> findAll();

    ResponseEntity<ResponseMessage> addPlan(Plan inputPlan);

    @Transactional
    ResponseEntity<ResponseMessage> updatePlan(PlanDTO PlanDTO, int id);

    @Transactional
    ResponseEntity<ResponseMessage> deletePlan(int id);

    @Transactional
    ResponseEntity<CustomerResponseMessage> removeCustomerFromPlan(int customerId, int PlanId);
}
