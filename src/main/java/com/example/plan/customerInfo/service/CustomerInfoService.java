package com.example.plan.customerInfo.service;

import com.example.plan.utils.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CustomerInfoService {
    List<Map<String, Object>> findCustomerInfo();

    ResponseEntity<ResponseMessage>  findById(Long id);

    //Map<String, Object> findCustomerByName(String firstname, String surname, LocalDate birthday);

    ResponseEntity<ResponseMessage> addCustomerInfo(Map<String, Object> requestMap, Long id);

    ResponseEntity<ResponseMessage> updateCustomerInfo(Map<String, Object> requestMap, Long id);


    ResponseEntity<ResponseMessage> calculateBMR(Long id);

    ResponseEntity<ResponseMessage> deleteCustomerInfo(Long id);

    ResponseEntity<ResponseMessage> deleteAllCustomerInfoByCustomerId(Long id);
}
