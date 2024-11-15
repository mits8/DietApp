package com.example.plan.plan.controller;

import com.example.plan.plan.service.PlanService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public List<Map<String, Object>> getPlanDetailsByCustomerfirstname (@RequestParam String firstname, @RequestParam String surname, @RequestParam LocalDate birthdate) {
       return  planService.getPlanDetailsByCustomerFullName(firstname, surname, birthdate);
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseMessage> count() {
        return planService.count();
    }

    @GetMapping("/generateReport")
    public ResponseEntity<ResponseMessage> generateReport(Map<String, Object> requestMap,
                                                          @RequestParam String firstname,
                                                          @RequestParam String surname,
                                                          @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
                                                          @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {
        return planService.generateReport(requestMap, firstname, surname, startDate, endDate);
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
    public ResponseEntity<ResponseMessage> addMealToPlan(@RequestBody Map<String, List<Object>> requestMap, @PathVariable String name, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        return planService.addMealToPlan(requestMap, name, startDate, endDate);
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<ResponseMessage> addCustomerToPlan(@RequestBody Map<String, List<Object>> requestMap, @RequestParam String name){
        return planService.addCustomerToPlan(requestMap, name);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updatePlan(@RequestBody Map<String, Object> requestMap, @PathVariable int id) {
        return planService.updatePlan(requestMap, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deletePlan(@PathVariable int id) {
        return planService.deletePlan(id);
    }

    @DeleteMapping("/delete/{planId}/customer/{customerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCustomerFromPlan(@PathVariable Long planId, @PathVariable int customerId) {
        return planService.removeCustomerFromPlan(planId, customerId);
    }

    @DeleteMapping("delete-from-plan/{planId}/meal/{mealId}/food/{foodId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> removeFoodFromMeal(@PathVariable int planId, @PathVariable int  mealId, @PathVariable int foodId) {
        return planService.removeFoodFromMeal(planId, mealId, foodId);
    }

    @DeleteMapping("/delete-from-plan/{planId}/both/meal/{mealId}/food/{foodId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteMealAndFood(@PathVariable int planId, @PathVariable int  mealId, @PathVariable int foodId) {
        return planService.deleteMealAndFood(planId, mealId, foodId);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<ResponseMessage> deleteAll() {
        return planService.deleteAll();
    }

}
