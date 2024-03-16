package com.example.plan.customerInfo.service.Impl;


import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customerInfo.entity.CustomerInfo;
import com.example.plan.customerInfo.repository.CustomerInfoRepository;
import com.example.plan.customerInfo.service.CustomerInfoService;
import com.example.plan.enums.Gender;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional
public class CustomerInfoServiceImpl implements CustomerInfoService {

    @Autowired
    private CustomerInfoRepository customerInfoRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Map<String, Object>> findCustomerInfo() {
        List<CustomerInfo> customerInfos = customerInfoRepository.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (CustomerInfo customerInfo : customerInfos) {
            Map<String, Object> customerInfoObjectMap = new HashMap<>();
            customerInfoObjectMap.put("id", customerInfo.getId());
            customerInfoObjectMap.put("createdDate", customerInfo.getCreatedDate());
            customerInfoObjectMap.put("age", customerInfo.getAge());
            customerInfoObjectMap.put("height", customerInfo.getHeight());
            customerInfoObjectMap.put("water", customerInfo.getWater());
            customerInfoObjectMap.put("weight", customerInfo.getWeight());
            customerInfoObjectMap.put("muscleMass", customerInfo.getMuscleMass());
            customerInfoObjectMap.put("bodyFatMass", customerInfo.getBodyFatMass());
            customerInfoObjectMap.put("fat", customerInfo.getFat());
            customerInfoObjectMap.put("customer_id", customerInfo.getCustomer().getId());
            mapList.add(customerInfoObjectMap);
        }
        return mapList;
    }

    @Override
    public Map<String, Object> findById(Long id) {
        Optional<CustomerInfo> existingCustomerInfo = customerInfoRepository.findById(id);
        if (existingCustomerInfo.isPresent()) {
            CustomerInfo customerInfo = existingCustomerInfo.get();
            Map<String, Object> customerInfoObjectMap = new HashMap<>();
            customerInfoObjectMap.put("id", customerInfo.getId());
            customerInfoObjectMap.put("createdDate", customerInfo.getCreatedDate());
            customerInfoObjectMap.put("age", customerInfo.getAge());
            customerInfoObjectMap.put("height", customerInfo.getHeight());
            customerInfoObjectMap.put("water", customerInfo.getWater());
            customerInfoObjectMap.put("weight", customerInfo.getWeight());
            customerInfoObjectMap.put("muscleMass", customerInfo.getMuscleMass());
            customerInfoObjectMap.put("bodyFatMass", customerInfo.getBodyFatMass());
            customerInfoObjectMap.put("fat", customerInfo.getFat());
            customerInfoObjectMap.put("customer_id", customerInfo.getCustomer());
            return  customerInfoObjectMap;
        } else {
            throw new RuntimeException("Ο πελάτης δεν βρέθηκε..");
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> addCustomerInfo(Map<String, Object> requestMap, Long id) {
        try {
            Optional<Customer> existingCustomer = customerRepository.findById(id);

            if (existingCustomer.isPresent()) {
                CustomerInfo customerInfo = new CustomerInfo();
                customerInfo.setCreatedDate(LocalDateTime.now());
                customerInfo.setAge((Integer) requestMap.get("age"));
                customerInfo.setActivityLevel((Integer) requestMap.get("activityLevel"));
                customerInfo.setHeight((Double) requestMap.get("height"));
                customerInfo.setWater((Double) requestMap.get("water"));
                customerInfo.setWeight((Double) requestMap.get("weight"));
                customerInfo.setMuscleMass((Double) requestMap.get("muscleMass"));
                customerInfo.setBodyFatMass((Double) requestMap.get("bodyFatMass"));
                customerInfo.setFat((Double) requestMap.get("fat"));
                customerInfo.setCustomer(existingCustomer.get());
                customerInfoRepository.save(customerInfo);

                String message = "Οι πληροφορίες του πελάτης " + "'" + customerInfo.getCustomer().getFirstName() + " " + customerInfo.getCustomer().getFirstName() + "'" + " γράφτηκαν επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateCustomerInfo(Map<String, Object> requestMap, Long id) {
        try{
            Optional<Customer> existingCustomer = Optional.ofNullable(customerRepository.findByEmail(String.valueOf(requestMap.get("email"))));
            Optional<CustomerInfo> existingCustomerInfo = customerInfoRepository.findById(id);
            if (existingCustomerInfo.isPresent()) {
                CustomerInfo updateCustomerInfo = existingCustomerInfo.get();
                    updateCustomerInfo.setAge((Integer) requestMap.get("age"));
                    updateCustomerInfo.setHeight((Double) requestMap.get("height"));
                    updateCustomerInfo.setWater((Double) requestMap.get("water"));
                    updateCustomerInfo.setWeight((Double) requestMap.get("weight"));
                    updateCustomerInfo.setMuscleMass((Double) requestMap.get("muscleMass"));
                    updateCustomerInfo.setBodyFatMass((Double) requestMap.get("bodyFatMass"));
                    updateCustomerInfo.setFat((Double) requestMap.get("fat"));
                    existingCustomer.ifPresent(updateCustomerInfo::setCustomer);

                    customerInfoRepository.save(updateCustomerInfo);

                String message = "Οι μετρήσεις του πελάτη ενημερώθηκαν επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response , HttpStatus.OK);
            }
            String message = "Ο πελάτης ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> calculateBMR(Long id) {
        try {
            Optional<CustomerInfo> optionalCustomerInfo = customerInfoRepository.findById(id);
            if (optionalCustomerInfo.isPresent()) {
                CustomerInfo customerInfo = optionalCustomerInfo.get();
                Customer customer = customerInfo.getCustomer();
                String formattedBmr = null;
                String formattedTdee = null;
                if (customer != null) {
                    int activityLevel = customerInfo.getActivityLevel();
                    double bmr = Utils.calculateBMR(customerInfo);
                    double tdee = Utils.calculateTDEE(bmr, activityLevel);
                    DecimalFormat df = new DecimalFormat("#0.00");
                    formattedBmr = df.format(bmr);
                    formattedTdee = df.format(tdee);
                    customerInfo.setBmr(Double.valueOf(formattedBmr));
                    customerInfo.setTdee(Double.valueOf(formattedTdee));
                    customerInfoRepository.save(customerInfo);
                }
                String message = "Οι μετρήσεις γράφτηκαν με επιτυχία!";
                ResponseMessage response = new ResponseMessage(message, "BMR: " + formattedBmr + "  " + "TDEE: " + formattedTdee);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "Οι μετρήσεις του πελάτη ΔΕΝ βρέθηκαν..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



    @Override
    public ResponseEntity<ResponseMessage> deleteCustomerInfo(Long id) {
        try {
            Optional<CustomerInfo> optionalCustomerInfo = customerInfoRepository.findById(id);
            if (optionalCustomerInfo.isPresent()) {
                customerInfoRepository.delete(optionalCustomerInfo.get());
                String message = "Οι μετρήσεις του πελάτη διαγράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "Οι μετρήσεις του πελάτη ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.error("Error deleting customer", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteAllCustomerInfoByCustomerId(Long id) {
        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                List<CustomerInfo> customerInfoList = customerInfoRepository.findByCustomer(optionalCustomer.get());
                customerInfoRepository.deleteAll(customerInfoList);
                String message = "Οι μετρήσεις του πελάτη διαγράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "Ο πελάτης ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.error("Error deleting customer", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
