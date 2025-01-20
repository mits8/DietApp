package com.example.plan.customer.service.Impl;

import com.example.plan.contactInfo.entity.ContactInfo;
import com.example.plan.contactInfo.repository.ContactInfoRepository;
import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customer.service.CustomerService;
import com.example.plan.customerInfo.entity.CustomerInfo;
import com.example.plan.enums.Gender;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.repository.PlanRepository;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import com.example.plan.utils.Utils;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.validation.Validation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PlanRepository PlanRepository;
    @Autowired
    private ContactInfoRepository customerContactInfoRepository;
    @Autowired
    private UserInfoRepository customerUserInfoRepository;
    @Autowired
    private Validation validation;

    @Autowired
    private UserInfoRepository userInfoRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<Map<String, Object>> findAllCustomers() {
        List<DietCustomer> customers = customerRepository.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (DietCustomer customer : customers) {
            Map<String, Object> customerObjectMap = new HashMap<>();
            customerObjectMap.put("id", customer.getId());
            customerObjectMap.put("firstname", customer.getFirstname());
            customerObjectMap.put("surname", customer.getSurname());
            customerObjectMap.put("city", customer.getCity());
            customerObjectMap.put("address", customer.getAddress());
            customerObjectMap.put("birthday", customer.getBirthday());
            customerObjectMap.put("gender", customer.getGender().toString());
            if (customer.getContactInfo() != null) {
                customerObjectMap.put("contactInfoId", customer.getContactInfo().getContactInfoId());
                customerObjectMap.put("mobilePhone", customer.getContactInfo().getMobilePhone());
                customerObjectMap.put("phone", customer.getContactInfo().getPhone());
                customerObjectMap.put("email", customer.getContactInfo().getEmail());
            }
            /*if (customer.getCustomerInfos() != null || customer.getContactInfo() == 0) {
                CustomerInfo customerInfos = customer.getCustomerInfos().get(0);
                customerObjectMap.put("age", customerInfos.getAge());
                customerObjectMap.put("height", customerInfos.getHeight());
                customerObjectMap.put("water", customerInfos.getWater());
                customerObjectMap.put("weight", customerInfos.getWeight());
                customerObjectMap.put("muscleMass", customerInfos.getMuscleMass());
                customerObjectMap.put("bodyFatMass", customerInfos.getBodyFatMass());
                customerObjectMap.put("fat", customerInfos.getFat());
                customerObjectMap.put("bmr", customerInfos.getBmr());
                customerObjectMap.put("tdee", customerInfos.getTdee());
                customerObjectMap.put("activityLevel", customerInfos.getActivityLevel());
            }*/
            mapList.add(customerObjectMap);
        }
        return mapList;
    }

    @Override
    public Map<String, Object> findContactInfosByCustomerId(Long id) {
        Map<String, Object> customerObjectMap = new HashMap<>();
        Optional<DietCustomer> existingCustomer = customerRepository.findById(id);
        if (existingCustomer.isPresent()) {
            List<CustomerInfo> customerInfos = existingCustomer.get().getCustomerInfos().stream().toList();
            for (CustomerInfo customerInfo : customerInfos) {
            customerObjectMap.put("age", customerInfo.getAge());
            customerObjectMap.put("height", customerInfo.getHeight());
            customerObjectMap.put("water", customerInfo.getWater());
            customerObjectMap.put("weight", customerInfo.getWeight());
            customerObjectMap.put("muscleMass", customerInfo.getMuscleMass());
            customerObjectMap.put("bodyFatMass", customerInfo.getBodyFatMass());
            customerObjectMap.put("fat", customerInfo.getFat());
            customerObjectMap.put("bmr", customerInfo.getBmr());
            customerObjectMap.put("tdee", customerInfo.getTdee());
            customerObjectMap.put("activityLevel", customerInfo.getActivityLevel());
            }
        }
        return customerObjectMap;
    }

    @Override
    public Map<String, Object> findById(Long id) {
        Optional<DietCustomer> existingCustomer = customerRepository.findById(id);
        if (existingCustomer.isPresent()) {
        DietCustomer customer = existingCustomer.get();
            Map<String, Object> customerObjectMap = new HashMap<>();
            customerObjectMap.put("id", String.valueOf(customer.getId()));
            customerObjectMap.put("firstname", customer.getFirstname());
            customerObjectMap.put("surname", customer.getSurname());
            customerObjectMap.put("city", customer.getCity());
            customerObjectMap.put("address", customer.getAddress());
            customerObjectMap.put("birthday", String.valueOf(customer.getBirthday()));
            customerObjectMap.put("gender", String.valueOf(customer.getGender()));
            customerObjectMap.put("contactInfo", customer.getContactInfo());
        return  customerObjectMap;
        } else {
            throw new RuntimeException("Ο πελάτης δεν βρέθηκε..");
        }
    }

    @Override
    public Map<String, Object> findCustomerByName(String firstname, String surname, LocalDate birthday) {
        DietCustomer customer = customerRepository.findCustomerByName(firstname, surname, birthday);

            Map<String, Object> customerObjectMap = new HashMap<>();
            customerObjectMap.put("id", customer.getId());
            customerObjectMap.put("firstname", customer.getFirstname());
            customerObjectMap.put("surname", customer.getSurname());
            customerObjectMap.put("city", customer.getCity());
            customerObjectMap.put("address", customer.getAddress());
            customerObjectMap.put("birthday", customer.getBirthday());
            customerObjectMap.put("gender", customer.getGender());

        return customerObjectMap;
    }

    @Override
    public ResponseEntity<ResponseMessage> addCustomer(Map<String, Object> requestMap) {
        try {
            DietCustomer existingCustomer = customerRepository.findByEmail((String) requestMap.get("email"));
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Integer userId = null;
            if (authentication != null && authentication.isAuthenticated()) {
                Object userDetails = authentication.getPrincipal();
                if (userDetails != null) {
                    String email = authentication.getName();
                    Optional<UserInfo> user = userInfoRepository.findByEmail(email);
                    userId = Math.toIntExact(user.get().getId());
                }
            }
            if (existingCustomer == null) {
//                if (validation.isValidNameLengthCustomer(requestMap)) {
//                    if (validation.isValidNumbersAndLengthCustomer(requestMap)) {
                        DietCustomer customer = new DietCustomer();
                        customer.setFirstname((String) requestMap.get("firstname"));
                        customer.setSurname((String) requestMap.get("surname"));
                        customer.setPassword(passwordEncoder.encode((String) requestMap.get("password")));
                        customer.setCity((String) requestMap.get("city"));
                        customer.setAddress((String) requestMap.get("address"));
                        customer.setBirthday(Utils.formatter(requestMap));
                        customer.setGender(Gender.valueOf((String) requestMap.get("gender")));

                        //Integer userId = (Integer) requestMap.get("user_id");

                        UserInfo userInfo = userInfoRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found."));
                        customer.setUserInfo(userInfo);
                        DietCustomer savedCustomer = customerRepository.save(customer);

                        if (requestMap.get("contactInfo") != null) {
                            ContactInfo contactInfo = new ContactInfo();
                            Map<String, Object> map = (Map<String, Object>) requestMap.get("contactInfo");
                            contactInfo.setMobilePhone((String) map.get("mobilePhone"));
                            contactInfo.setEmail((String) map.get("email"));
                            contactInfo.setPhone((String) map.get("phone"));
                            contactInfo.setDietCustomer(savedCustomer);
                            customerContactInfoRepository.save(contactInfo);
                        }

                        String message = "Ο πελάτης " + "'" + customer.getFirstname() + " " + customer.getSurname() + "'" + " γράφτηκε επιτυχώς!";
                        ResponseMessage response = new ResponseMessage(message, requestMap);
                        return new ResponseEntity<>(response, HttpStatus.CREATED);
                    /*} else {
                        String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                        ResponseMessage response = new ResponseMessage(message, null);
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }*/
                /*} else {
                    String message = "Οι χαρακτήρες πρέπει να είναι γράμματα '5-30'..";
                    ResponseMessage response = new ResponseMessage(message, null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }*/
            }
            String message = "Ο πελάτης με email" + "'" + requestMap.get("email") + "'" + " υπάρχει ήδη..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Error adding customer: {}", ex.getMessage());
        }

        String message = "Τα βασικά στοιχεία είναι υποχρεωτικά..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateCustomer(Map<String, Object> requestMap, Long id) {
        try {
            Optional<DietCustomer> existingCustomer = customerRepository.findById(id);
            if (existingCustomer.isPresent()) {
                DietCustomer updateCustomer = existingCustomer.get();
                if (validation.isValidNameLengthCustomer(requestMap)) {
                updateCustomer.setFirstname((String) requestMap.get("firstname"));
                updateCustomer.setSurname((String) requestMap.get("surname"));
                }else {
                    String message = "Οι χαρακτήρες πρέπει να είναι γράμματα  '5-30'..";
                    ResponseMessage response = new ResponseMessage(message, null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                updateCustomer.setCity((String) requestMap.get("city"));
                updateCustomer.setAddress((String) requestMap.get("address"));
                updateCustomer.setBirthday(Utils.formatter(requestMap));
                updateCustomer.setGender(Gender.valueOf((String) requestMap.get("gender")));
                customerRepository.save(updateCustomer);

                String message = "Ο πελάτης " + "'" + requestMap.get("firstname") + " " + requestMap.get("surname") + "'" + " ενημερώθηκε επιτυχώς!";
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
    public ResponseEntity<ResponseMessage> deleteCustomer(Long id) {
        try {
            Optional<DietCustomer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                customerRepository.delete(optionalCustomer.get());
                String message = "Ο πελάτης " + "'" + optionalCustomer.get().getFirstname() + " " + optionalCustomer.get().getSurname() + "'" + " διαγράφτηκε επιτυχώς!";
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

    @Override
    public ResponseEntity<ResponseMessage> deleteCustomerAndPlanById(Long customerId, int PlanId) {
        try {
            Optional<DietCustomer> existingCustomer = customerRepository.findById(customerId);
            Optional<Plan> existingPlan = PlanRepository.findById(PlanId);
            if (existingCustomer.isPresent()) {
                if (existingPlan.isPresent()) {
                    PlanRepository.deleteById(PlanId);
                    customerRepository.deleteById(customerId);
                }
                String message = "Ο πελάτης " + "'" + existingCustomer.get().getFirstname() + " " + existingCustomer.get().getSurname() + "'" + "και το πλάνο " + "'" + existingPlan.get().getName() + "'" + " διαγράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<> (response, HttpStatus.OK);
            }
            String message = "Ο πελάτης ή το πλάνο ΔΕΝ βρέθηκαν..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> customerData() {
        int countCustomers = customerRepository.countCustomer();
        List<Map<String, Object>> customers = customerRepository.customerData()
                .stream().map(c -> {
                    Map<String, Object> customerMap = new HashMap<>();
                    String firstname = (String) c.get("firstname");
                    customerMap.put("firstname", firstname);
                    String surname = (String) c.get("surname");
                    customerMap.put("surname", surname);
                    String fileName = customerRepository.findFilename(firstname, surname);
                    boolean exists = c.get("customerEmail") != null && fileName != null;
                    if (exists) {
                        customerMap.put("customerEmail", c.get("customerEmail"));
                        customerMap.put("fileName", fileName + "_Report.pdf");
                    }
                    customerMap.put("body", "Plan Programme");
                    return customerMap;
                }).toList();
        if (customers.size() != countCustomers) {
            String message = "cont?";
            ResponseMessage response = new ResponseMessage(message, customers);
            return new ResponseEntity<> (response, HttpStatus.OK);
        }
        String message = "Οι  πελάτες βρέθηκαν!";
        ResponseMessage response = new ResponseMessage(message, customers);
        return new ResponseEntity<> (response, HttpStatus.OK);
    }
}
