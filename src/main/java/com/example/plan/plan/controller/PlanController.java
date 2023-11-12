package com.example.plan.plan.controller;

import com.example.plan.dto.plan.PlanMealCustomerDTO;
import com.example.plan.plan.service.PlanService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    @GetMapping("/findAll")
    public ResponseEntity<List<PlanMealCustomerDTO>> findAll(){
        return new ResponseEntity<>(planService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findByCustomerName")
    public List<Object> getPlanDetailsByCustomerFirstName (@RequestParam String customerFirstname) {
        return planService.getPlanDetailsByCustomerFirstName(customerFirstname);
    }

    @PostMapping("/savePlan/{id}")
    public ResponseEntity<ResponseMessage> addToPlan(@RequestBody Map<String, List<Object>> requestMap,@PathVariable int id){
        return planService.addToPlan(requestMap,id);
    }


    @PostMapping("/saveMeal/{id}")
    public ResponseEntity<ResponseMessage> addMealToPlan(@RequestBody Map<String, List<Object>> requestMap, @PathVariable int id){
        return planService.addMealToPlan(requestMap, id);
    }

    @PostMapping("/saveCustomer/{id}")
    public ResponseEntity<ResponseMessage> addCustomerToPlan(@RequestBody Map<String, List<Object>> requestMap, @PathVariable int id){
        return planService.addCustomerToPlan(requestMap, id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updatePlan(@RequestBody Map<String, String> requestMap, @PathVariable int id) {
        return planService.updatePlan(requestMap, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deletePlan(@PathVariable int id) {
        return planService.deletePlan(id);
    }

    @DeleteMapping("/delete/{planId}/customer/{customerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCustomerFromPlan(@PathVariable int planId, @PathVariable int customerId) {
        return planService.removeCustomerFromPlan(planId, customerId);
    }
}
