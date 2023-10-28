package com.example.plan.weeklyPlan.controller;

import com.example.plan.utils.ResponseMessage;
import com.example.plan.dto.weeklyPlan.WeeklyPlanMealCustomerDTO;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import com.example.plan.weeklyPlan.service.WeeklyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseMessage> save(@RequestBody WeeklyPlan weeklyPlan){
        return weeklyPlanService.save(weeklyPlan);
    }
}
