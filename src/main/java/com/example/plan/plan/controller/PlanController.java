package com.example.plan.plan.controller;

import com.example.plan.dto.plan.PlanDTO;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.dto.plan.PlanMealCustomerDTO;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/findAll")
    public ResponseEntity<List<PlanMealCustomerDTO>> findAll(){
        return new ResponseEntity<>(planService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addPlan(@RequestBody Plan plan){
        return planService.addPlan(plan);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updatePlan(@RequestBody PlanDTO planDTO, @PathVariable int id) {
        return planService.updatePlan(planDTO, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deletePlan(@PathVariable int id) {
        return planService.deletePlan(id);
    }

    @DeleteMapping("/delete/{planId}/customer/{customerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponseMessage> deleteCustomerFromPlan(@PathVariable int planId, @PathVariable int customerId) {
        return planService.removeCustomerFromPlan(planId, customerId);
    }
}
