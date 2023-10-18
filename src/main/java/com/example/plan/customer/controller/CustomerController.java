package com.example.plan.customer.controller;

import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.service.CustomerService;
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
    public ResponseEntity<List<Customer>> findAll() {
        return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Customer> getById(@PathVariable int id) {
        return new ResponseEntity<>(customerService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateCustomer(@RequestBody Customer customer, @PathVariable int id) {
        return customerService.updateCustomer(customer, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCustomer(@PathVariable int  id) {
        return customerService.deleteCustomer(id);
    }
}
