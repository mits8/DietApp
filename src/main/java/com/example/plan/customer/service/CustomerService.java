package com.example.plan.customer.service;

import com.example.plan.customer.entity.Customer;
import com.example.plan.dto.CustomerDTO;
import com.example.plan.dto.CustomerWeeklyPlanDTO;
import com.example.plan.utils.CustomerResponseMessage;
import com.example.plan.utils.CustomerWeeklyPlanResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> findAll();

    List<CustomerWeeklyPlanDTO> findCustomerWithWeeklyPlan();

    CustomerDTO findById(int id);

/*    ResponseEntity<String> save(Customer customer);

    ResponseEntity<String> saveCustomerWithWeeklyPlan(Customer customer);*/

    ResponseEntity<String> updateCustomer(CustomerDTO customerDTO, int id);

//    ResponseEntity<String> deleteCustomer(int id);

    ResponseEntity<String> deleteCustomerAndWeeklyPlanById(int  customerId, int weeklyPlanId);

    /*--------------------------------------------------*/
    @Transactional
    ResponseEntity<CustomerResponseMessage> addCustomer(CustomerDTO customerDTO);

    @Transactional
    ResponseEntity<CustomerWeeklyPlanResponseMessage> addCustomerWithWeeklyPlan(CustomerWeeklyPlanDTO customerWeeklyPlanDTO);

    ResponseEntity<CustomerResponseMessage> deleteCustomer(int id);
}
