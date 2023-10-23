package com.example.plan.customer.controller;

import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.service.CustomerService;
import com.example.plan.dto.CustomerDTO;
import com.example.plan.dto.CustomerWeeklyPlanDTO;
import com.example.plan.utils.CustomerResponseMessage;
import com.example.plan.utils.CustomerWeeklyPlanResponseMessage;
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

   /* @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @PostMapping("/save/customer/plan")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createCustomerWithWeeklyPlan(@RequestBody Customer customer) {
        return customerService.saveCustomerWithWeeklyPlan(customer);
    }*/

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerDTO customer, @PathVariable int id) {
        return customerService.updateCustomer(customer, id);
    }

    /*@DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCustomer(@PathVariable int  id) {
        return customerService.deleteCustomer(id);
    }*/
    @DeleteMapping("/delete/customer/{customerId}/weekly-plan/{weeklyPlanId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCustomerAndWeeklyPlanById(@PathVariable int  customerId, @PathVariable int weeklyPlanId) {
        return customerService.deleteCustomerAndWeeklyPlanById(customerId, weeklyPlanId);
    }

    /*--------------------------------------------------*/

    @PostMapping("/addCustomer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponseMessage> addCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.addCustomer(customerDTO);
    }

    @PostMapping("/save/customer/weekly-plan")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerWeeklyPlanResponseMessage> addCustomerWithWeeklyPlan(@RequestBody CustomerWeeklyPlanDTO customerWeeklyPlanDTO) {
        return customerService.addCustomerWithWeeklyPlan(customerWeeklyPlanDTO);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerResponseMessage> deleteCustomer(@PathVariable int  id) {
        return customerService.deleteCustomer(id);
    }

}
