package com.example.plan.customer.service;

import com.example.plan.customer.entity.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();

    Customer findById(int id);

    ResponseEntity<String> save(Customer customer);

    ResponseEntity<String> updateCustomer(Customer customer, int id);

    ResponseEntity<String> deleteCustomer(int id);
}
