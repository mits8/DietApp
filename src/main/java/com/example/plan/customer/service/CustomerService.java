package com.example.plan.customer.service;

import com.example.plan.utils.ResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Transactional
public interface CustomerService {
    List<Map<String, Object>> findAllCustomers();

    Map<String, Object>  findById(int id);

    Map<String, Object> findCustomerByName(String firstName, String lastName, LocalDate birthday);

    ResponseEntity<ResponseMessage> addCustomer(Map<String, Object> requestMap);

    ResponseEntity<ResponseMessage> updateCustomer(Map<String, Object> requestMap, int id);

    ResponseEntity<ResponseMessage> deleteCustomerAndPlanById(int  customerId, int PlanId);

    ResponseEntity<ResponseMessage> deleteCustomer(int id);

}
