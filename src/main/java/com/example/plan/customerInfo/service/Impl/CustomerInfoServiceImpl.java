package com.example.plan.customerInfo.service.Impl;


import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customerInfo.entity.CustomerInfo;
import com.example.plan.customerInfo.repository.CustomerInfoRepository;
import com.example.plan.customerInfo.service.CustomerInfoService;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        return customerInfoRepository.findAll().stream()
                .map(customerInfo -> {
                    String fullName;
                    Map<String, Object> customerInfoObjectMap = new HashMap<>();
                    customerInfoObjectMap.put("id", customerInfo.getId());
                    customerInfoObjectMap.put("createdDate", customerInfo.getCreatedDate());
                    if (customerInfo.getCustomer() != null) {
                        fullName = customerInfo.getCustomer().getFirstname() + " " + customerInfo.getCustomer().getSurname();
                        customerInfoObjectMap.put("fullName", fullName);
                    }
                    customerInfoObjectMap.put("age", customerInfo.getAge());
                    customerInfoObjectMap.put("height", customerInfo.getHeight());
                    customerInfoObjectMap.put("water", customerInfo.getWater());
                    customerInfoObjectMap.put("weight", customerInfo.getWeight());
                    customerInfoObjectMap.put("muscleMass", customerInfo.getMuscleMass());
                    customerInfoObjectMap.put("bodyFatMass", customerInfo.getBodyFatMass());
                    customerInfoObjectMap.put("fat", customerInfo.getFat());
                    customerInfoObjectMap.put("customer_id", customerInfo.getCustomer().getId());
                    return customerInfoObjectMap;
                }).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<ResponseMessage> findById(Long id) {
        List<Map<String, Object>> customerInfoList = new ArrayList<>();

        try {
            Optional<DietCustomer> existingCustomer = customerRepository.findById(id);
            if (existingCustomer.isPresent()) {
                customerInfoList = customerInfoRepository.findCustomerInfosByCustomer(id).stream()
                        .map(customerInfo -> {
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
                            return customerInfoObjectMap;
                        })
                        .collect(Collectors.toList());

                String message = "Customer details retrieved successfully!";
                ResponseMessage responseMessage = new ResponseMessage(message, customerInfoList);
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            } else {
                String message = "Customer not found.";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error retrieving customer info: ", e);
            String errorMessage = "An error occurred while retrieving customer information.";
            return new ResponseEntity<>(new ResponseMessage(errorMessage, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> addCustomerInfo(Map<String, Object> requestMap, Long id) {
        try {
            Optional<DietCustomer> existingCustomer = customerRepository.findById(id);

            if (existingCustomer.isPresent()) {
                CustomerInfo customerInfo = new CustomerInfo();
                customerInfo.setCreatedDate(LocalDate.now());
                customerInfo.setHeight(Double.valueOf((String) requestMap.get("height")));
                customerInfo.setWeight(Double.valueOf((String) requestMap.get("weight")));
                //customerInfo.setWater(Double.valueOf((String) requestMap.get("water")));
                customerInfo.setMuscleMass(Double.valueOf((String) requestMap.get("muscleMass")));
                customerInfo.setBodyFatMass(Double.valueOf((String) requestMap.get("bodyFatMass")));
                customerInfo.setFat(Double.valueOf((String) requestMap.get("fat")));
                //customerInfo.setActivityLevel(Integer.valueOf((String) requestMap.get("activityLevel")));
                customerInfo.setCustomer(existingCustomer.get());
                customerInfoRepository.save(customerInfo);

                String message = "Οι πληροφορίες του πελάτης " + "'" + customerInfo.getCustomer().getFirstname() + " " + customerInfo.getCustomer().getFirstname() + "'" + " γράφτηκαν επιτυχώς!";
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
            Optional<DietCustomer> existingCustomer = Optional.ofNullable(customerRepository.findByEmail(String.valueOf(requestMap.get("email"))));
            Optional<CustomerInfo> existingCustomerInfo = customerInfoRepository.findById(id);
            if (existingCustomerInfo.isPresent()) {
                CustomerInfo updateCustomerInfo = existingCustomerInfo.get();
                updateCustomerInfo.setHeight(getDoubleValue(requestMap, "height"));
                updateCustomerInfo.setWeight(getDoubleValue(requestMap, "weight"));
                updateCustomerInfo.setBodyFatMass(getDoubleValue(requestMap, "bodyFatMass"));
                updateCustomerInfo.setFat(getDoubleValue(requestMap, "fat"));
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

    private Double getDoubleValue(Map<String, Object> requestMap, String key) {
        Object value = requestMap.get(key);
        if (value instanceof String) {
            return Double.valueOf((String) value);
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    @Override
    public ResponseEntity<ResponseMessage> calculateBMR(Long id) {
        try {
            Optional<CustomerInfo> optionalCustomerInfo = customerInfoRepository.findById(id);
            if (optionalCustomerInfo.isPresent()) {
                CustomerInfo customerInfo = optionalCustomerInfo.get();
                DietCustomer customer = customerInfo.getCustomer();
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
            Optional<DietCustomer> optionalCustomer = customerRepository.findById(id);
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
