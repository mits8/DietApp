package com.example.plan.customer.controller;

import com.example.plan.customer.service.CustomerService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> findAllCustomers() {
        return new ResponseEntity<>(customerService.findAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object> > findById(@PathVariable Long id) {
        return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/fullName")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> findCustomerByName(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("birthday") LocalDate birthday ) {
        return new ResponseEntity<>(customerService.findCustomerByName(firstName, lastName, birthday), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> addCustomer(@RequestBody Map<String, Object> requestMap) {
        return customerService.addCustomer(requestMap);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> updateCustomer(@RequestBody Map<String, Object> requestMap, @PathVariable Long id) {
        return customerService.updateCustomer(requestMap, id);
    }

    @DeleteMapping("/delete/customer/{customerId}/plan/{planId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCustomerAndPlanById(@PathVariable Long  customerId, @PathVariable int planId) {
        return customerService.deleteCustomerAndPlanById(customerId, planId);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCustomer(@PathVariable Long  id) {
        return customerService.deleteCustomer(id);
    }

}
