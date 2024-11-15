package com.example.plan.contactInfo.service;

import com.example.plan.utils.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ContactInfoService {
    List<Map<String, Object>> findAllContactInfo();

    ResponseEntity<ResponseMessage> getContactInfoById(Map<String, Object> requestMap,Long id);

    ResponseEntity<ResponseMessage> addContactInfoByCustomerId(Map<String, Object> requestMap, Long id);

    ResponseEntity<ResponseMessage> deleteContactInfo(Long id);
}
