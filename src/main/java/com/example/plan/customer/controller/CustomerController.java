package com.example.plan.customer.controller;

import com.example.plan.customer.service.CustomerService;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerPlanDTO;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Object>> findAllCustomers() {
        return new ResponseEntity<>(customerService.findAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("/find/customer-plan")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerPlanDTO>> findCustomerWithPlan() {
        return new ResponseEntity<>(customerService.findCustomerWithPlan(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerDTO> findById(@PathVariable int id) {
        return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/name")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerDTO> findByName(@RequestParam("lastName") String lastName) {
        return new ResponseEntity<>(customerService.findByName(lastName), HttpStatus.OK);
    }

    @GetMapping("/fullName")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CustomerDTO> findCustomerByName(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName) {
        return new ResponseEntity<>(customerService.findCustomerByName(firstName, lastName), HttpStatus.OK);
    }

    @PostMapping("/addCustomer")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> addCustomer(@RequestBody Map<String, String> requestMap) {
        return customerService.addCustomer(requestMap);
    }

    @PostMapping("/save/customer-plan")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> addCustomerWithPlan(@RequestBody CustomerPlanDTO customerPlanDTO) {
        return customerService.addCustomerWithPlan(customerPlanDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> updateCustomer(@RequestBody Map<String, String> requestMap, @PathVariable int id) {
        return customerService.updateCustomer(requestMap, id);
    }

    @DeleteMapping("/delete/customer/{customerId}/plan/{planId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCustomerAndPlanById(@PathVariable int  customerId, @PathVariable int planId) {
        return customerService.deleteCustomerAndPlanById(customerId, planId);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCustomer(@PathVariable int  id) {
        return customerService.deleteCustomer(id);
    }

}
