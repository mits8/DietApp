package com.example.plan.customer.service.Impl;

import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customer.service.CustomerService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PlanRepository PlanRepository;
    @Autowired
    private Validation validation;

    @Autowired
    private UserInfoRepository userInfoRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public  List<Map<String, Object>> findAllCustomers() {
        List<DietCustomer> customers = customerRepository.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (DietCustomer customer : customers) {
            Map<String, Object> customerObjectMap = new HashMap<>();
            customerObjectMap.put("id", customer.getId());
            customerObjectMap.put("firstname", customer.getFirstname());
            customerObjectMap.put("surname", customer.getSurname());
            customerObjectMap.put("email", customer.getEmail());
            customerObjectMap.put("phone", customer.getPhone());
            customerObjectMap.put("city", customer.getCity());
            customerObjectMap.put("address", customer.getAddress());
            customerObjectMap.put("birthday", customer.getBirthday());
            customerObjectMap.put("gender", customer.getGender());
            mapList.add(customerObjectMap);
        }
        return mapList;
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
            customerObjectMap.put("email", customer.getEmail());
            customerObjectMap.put("phone", customer.getPhone());
            customerObjectMap.put("city", customer.getCity());
            customerObjectMap.put("address", customer.getAddress());
            customerObjectMap.put("birthday", String.valueOf(customer.getBirthday()));
            customerObjectMap.put("gender", String.valueOf(customer.getGender()));
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
            customerObjectMap.put("email", customer.getEmail());
            customerObjectMap.put("phone", customer.getPhone());
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
            if (existingCustomer == null) {
                if (validation.isValidNameLengthCustomer(requestMap)) {
                    if (validation.isValidNumbersAndLengthCustomer(requestMap)) {
                        DietCustomer customer = new DietCustomer();
                        customer.setFirstname((String) requestMap.get("firstname"));
                        customer.setSurname((String) requestMap.get("surname"));
                        customer.setEmail((String) requestMap.get("email"));
                        customer.setPassword(passwordEncoder.encode((String) requestMap.get("password")));
                        customer.setPhone((String) requestMap.get("phone"));
                        customer.setCity((String) requestMap.get("city"));
                        customer.setAddress((String) requestMap.get("address"));
                        customer.setBirthday(Utils.formatter(requestMap));
                        customer.setGender(Gender.valueOf((String) requestMap.get("gender")));

                        Integer userId = (Integer) requestMap.get("user_id");
                        UserInfo userInfo = userInfoRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
                        customer.setUserInfo(userInfo);
                        customerRepository.save(customer);

                        String message = "Ο πελάτης " + "'" + customer.getFirstname() + " " + customer.getSurname() + "'" + " γράφτηκε επιτυχώς!";
                        ResponseMessage response = new ResponseMessage(message, requestMap);
                        return new ResponseEntity<>(response, HttpStatus.CREATED);
                    } else {
                        String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                        ResponseMessage response = new ResponseMessage(message, null);
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    String message = "Οι χαρακτήρες πρέπει να είναι γράμματα '5-30'..";
                    ResponseMessage response = new ResponseMessage(message, null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }
            String message = "Ο πελάτης με email" + "'" + requestMap.get("email") + "'" + " υπάρχει ήδη..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
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
                if (!Objects.equals(updateCustomer.getEmail(), requestMap.get("email"))) {
                    updateCustomer.setEmail((String) requestMap.get("email"));
                }
                if (validation.isValidNumbersAndLengthCustomer(requestMap)) {
                updateCustomer.setPhone((String) requestMap.get("phone"));
                } else {
                    String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                    ResponseMessage response = new ResponseMessage(message, null);
                    return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
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
}
