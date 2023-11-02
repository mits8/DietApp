package com.example.plan.weeklyPlan.controller;

import com.example.plan.dto.weeklyPlan.WeeklyPlanDTO;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.dto.weeklyPlan.WeeklyPlanMealCustomerDTO;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import com.example.plan.weeklyPlan.service.WeeklyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weeklyPlan")
public class WeeklyPlanController {

    @Autowired
    private WeeklyPlanService weeklyPlanService;

    @GetMapping("/findAll")
    public ResponseEntity<List<WeeklyPlanMealCustomerDTO>> findAll(){
        return new ResponseEntity<>(weeklyPlanService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addWeeklyPlan(@RequestBody WeeklyPlan weeklyPlan){
        return weeklyPlanService.addWeeklyPlan(weeklyPlan);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateWeeklyPlan(@RequestBody WeeklyPlanDTO weeklyPlanDTO, @PathVariable int id) {
        return weeklyPlanService.updateWeeklyPlan(weeklyPlanDTO, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteWeeklyPlan(@PathVariable int id) {
        return weeklyPlanService.deleteWeeklyPlan(id);
    }

    @DeleteMapping("/delete/{customerId}from-weeklyPlan/{weeklyPlanId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponseMessage> deleteCustomerFromWeeklyPlan(@PathVariable int customerId, @PathVariable int weeklyPlanId) {
        return weeklyPlanService.removeCustomerFromWeeklyPlan(customerId, weeklyPlanId);
    }
}
