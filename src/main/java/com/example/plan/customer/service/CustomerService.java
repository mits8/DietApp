package com.example.plan.customer.service;

import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerWeeklyPlanDTO;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.utils.customer.CustomerWeeklyPlanResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> findAll();

    List<CustomerWeeklyPlanDTO> findCustomerWithWeeklyPlan();

    CustomerDTO findById(int id);


    ResponseEntity<CustomerResponseMessage> updateCustomer(CustomerDTO customerDTO, int id);

//    ResponseEntity<String> deleteCustomer(int id);

    ResponseEntity<CustomerWeeklyPlanResponseMessage> deleteCustomerAndWeeklyPlanById(int  customerId, int weeklyPlanId);

    /*--------------------------------------------------*/
    @Transactional
    ResponseEntity<CustomerResponseMessage> addCustomer(CustomerDTO customerDTO);

    @Transactional
    ResponseEntity<CustomerWeeklyPlanResponseMessage> addCustomerWithWeeklyPlan(CustomerWeeklyPlanDTO customerWeeklyPlanDTO);

    ResponseEntity<CustomerResponseMessage> deleteCustomer(int id);
}
