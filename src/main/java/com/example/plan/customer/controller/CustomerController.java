package com.example.plan.customer.controller;

import com.example.plan.customer.service.CustomerService;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerWeeklyPlanDTO;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.utils.customer.CustomerWeeklyPlanResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerDTO>> findAll() {
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/find/customer-weekly_plan")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerWeeklyPlanDTO>> findCustomerWithWeeklyPlan() {
        return new ResponseEntity<>(customerService.findCustomerWithWeeklyPlan(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerDTO> getById(@PathVariable int id) {
        return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/addCustomer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponseMessage> addCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.addCustomer(customerDTO);
    }

    @PostMapping("/save/customer-weekly-plan")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerWeeklyPlanResponseMessage> addCustomerWithWeeklyPlan(@RequestBody CustomerWeeklyPlanDTO customerWeeklyPlanDTO) {
        return customerService.addCustomerWithWeeklyPlan(customerWeeklyPlanDTO);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponseMessage> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable int id) {
        return customerService.updateCustomer(customerDTO, id);
    }

    @DeleteMapping("/delete/customer/{customerId}/weekly-plan/{weeklyPlanId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerWeeklyPlanResponseMessage> deleteCustomerAndWeeklyPlanById(@PathVariable int  customerId, @PathVariable int weeklyPlanId) {
        return customerService.deleteCustomerAndWeeklyPlanById(customerId, weeklyPlanId);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponseMessage> deleteCustomer(@PathVariable int  id) {
        return customerService.deleteCustomer(id);
    }
}
