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

    Map<String, Object> findContactInfosByCustomerId(Long id);

    Map<String, Object>  findById(Long id);

    Map<String, Object> findCustomerByName(String firstname, String surname, LocalDate birthday);

    ResponseEntity<ResponseMessage> addCustomer(Map<String, Object> requestMap);

    ResponseEntity<ResponseMessage> updateCustomer(Map<String, Object> requestMap, Long id);

    ResponseEntity<ResponseMessage> deleteCustomerAndPlanById(Long  customerId, int PlanId);

    ResponseEntity<ResponseMessage> deleteCustomer(Long id);

    ResponseEntity<ResponseMessage> customerData();
}
