package com.example.plan.customer.service;

import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerPlanDTO;
import com.example.plan.utils.customer.CustomerResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> findAll();

    List<CustomerPlanDTO> findCustomerWithPlan();

    CustomerDTO findById(int id);

    CustomerDTO findByName(String lastName);

    CustomerDTO findCustomerByName(String firstName, String lastName);

    @Transactional
    ResponseEntity<CustomerResponseMessage> addCustomer(CustomerDTO customerDTO);

    @Transactional
    ResponseEntity<CustomerResponseMessage> addCustomerWithPlan(CustomerPlanDTO customerPlanDTO);

    ResponseEntity<CustomerResponseMessage> updateCustomer(CustomerDTO customerDTO, int id);

    ResponseEntity<CustomerResponseMessage> deleteCustomerAndPlanById(int  customerId, int PlanId);

    ResponseEntity<CustomerResponseMessage> deleteCustomer(int id);

}
