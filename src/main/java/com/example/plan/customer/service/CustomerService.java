package com.example.plan.customer.service;

import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerPlanDTO;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.customer.CustomerResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<CustomerDTO> findAll();

    List<CustomerPlanDTO> findCustomerWithPlan();

    CustomerDTO findById(int id);

    CustomerDTO findByName(String lastName);

    CustomerDTO findCustomerByName(String firstName, String lastName);

    @Transactional
    ResponseEntity<ResponseMessage> addCustomer(Map<String, String> requestMap);

    @Transactional
    ResponseEntity<CustomerResponseMessage> addCustomerWithPlan(CustomerPlanDTO customerPlanDTO);

    ResponseEntity<CustomerResponseMessage> deleteCustomerAndPlanById(int  customerId, int PlanId);

    @Transactional
    ResponseEntity<ResponseMessage> updateCustomer(Map<String, String> requestMap, int id);

    ResponseEntity<CustomerResponseMessage> deleteCustomer(int id);

}
