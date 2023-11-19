package com.example.plan.plan.controller;

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
    public ResponseEntity<List<Map<String, Object>>> findAll(){
        return new ResponseEntity<>(planService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/find/plan/meal/food")
    public ResponseEntity<List<Map<String, Object>>> findPlanMealFood(){
        return new ResponseEntity<>(planService.findPlanMealFood(), HttpStatus.OK);
    }

    @GetMapping("/find/Customer/byName")
    public List<Map<String, Object>> getPlanDetailsByCustomerFirstName (@RequestParam String firstname, @RequestParam String lastname) {
       return  planService.getPlanDetailsByCustomerFullName(firstname, lastname);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addPlan(@RequestBody Map<String, Object> requestMap){
        return planService.addPlan(requestMap);
    }

    @PostMapping("/addPlan/{id}")
    public ResponseEntity<ResponseMessage> addToPlan(@RequestBody Map<String, List<Object>> requestMap, @PathVariable int id){
        return planService.addToPlan(requestMap,id);
    }


    @PostMapping("/addMeal/{name}")
    public ResponseEntity<ResponseMessage> addMealToPlan(@RequestBody Map<String, List<Object>> requestMap, @PathVariable String name){
        return planService.addMealToPlan(requestMap, name);
    }

    @PostMapping("/addCustomer/{id}")
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
