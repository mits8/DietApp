package com.example.plan.customer.service.Impl;

import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customer.service.CustomerService;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerPlanDTO;
import com.example.plan.enums.Gender;
import com.example.plan.map.Mapper;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.repository.PlanRepository;
import com.example.plan.utils.PlanUtils;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.validation.Validation;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PlanRepository PlanRepository;


    @Autowired
    private Mapper mapper;

    @Autowired
    private Validation validation;


    @Override
    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS= customers.stream()
                .map(mapper::mapCustomerToCustomerDTO)
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<CustomerPlanDTO> findCustomerWithPlan() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerPlanDTO> customerPlanDTOS= customers.stream()
                .map(mapper::customerPlanDTO)
                .collect(Collectors.toList());
        return customerPlanDTOS;
    }


    @Override
    public CustomerDTO findById(int id) {
        Optional<Customer> existingCustomer = customerRepository.findById(id);
        if (existingCustomer.isPresent()) {
        Customer customer = existingCustomer.get();
        CustomerDTO customerDTO = mapper.mapCustomerToCustomerDTO(customer);
        return  customerDTO;
        } else {
            throw new RuntimeException("Ο πελάτης δεν βρέθηκε..");
        }
    }

    @Override
    public CustomerDTO findByName(String lastName) {
        Customer customer = customerRepository.findByName(lastName);
        CustomerDTO customerDTO = mapper.mapCustomerToCustomerDTO(customer);
        return customerDTO;
    }

    @Override
    public CustomerDTO findCustomerByName(String firstName, String lastName) {
        Customer customer = customerRepository.findCustomerByName(firstName, lastName);
        CustomerDTO customerDTO = mapper.mapCustomerToCustomerDTO(customer);
        return customerDTO;
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addCustomer(Map<String, String> requestMap) {
        try {
            Customer email = customerRepository.findByEmailMap(requestMap.get("email"));
            if (Objects.isNull(email)) {
                if (validation.isValidNameLengthCustomerDTO(requestMap)) {
                    if (validation.isValidNumbersAndLengthCustomerDTO(requestMap)) {
                        Customer customer = new Customer();
                        customer.setFirstName(requestMap.get("firstName"));
                        customer.setLastName(requestMap.get("lastName"));
                        customer.setEmail(requestMap.get("email"));
                        customer.setPhone(requestMap.get("phone"));
                        customer.setCity(requestMap.get("city"));
                        customer.setAddress(requestMap.get("address"));
                        customer.setBirthday(PlanUtils.formatter(requestMap));
                        customer.setGender(Gender.valueOf(requestMap.get("gender")));
                        customerRepository.save(customer);

                        String message = "Ο πελάτης " + "'" + customer.getFirstName() + " " + customer.getLastName() + "'" + " γράφτηκε επιτυχώς!";
                        ResponseMessage response = new ResponseMessage(message, requestMap);
                        return new ResponseEntity<>(response, HttpStatus.CREATED);
                    } else {
                        String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                        ResponseMessage response = new ResponseMessage(message, (CustomerDTO) null);
                        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                    }
                }else {
                    String message = "Οι χαρακτήρες πρέπει να είναι γράμματα '5-30'..";
                    ResponseMessage response = new ResponseMessage(message, (CustomerDTO) null);
                    return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                }
            } else {
                String message = "Ο πελάτης με email" + "'" + requestMap.get("email") + "'" + " υπάρχει ήδη..";
                ResponseMessage response = new ResponseMessage(message, (CustomerDTO) null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Τα βασικά στοιχεία είναι υποχρεωτκά..";
        ResponseMessage response = new ResponseMessage(message, (CustomerDTO) null);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<CustomerResponseMessage> addCustomerWithPlan(CustomerPlanDTO customerPlanDTO) {
        try {
            Customer email = customerRepository.findByEmail(customerPlanDTO.getEmail());
            if (Objects.isNull(email)) {
                if (validation.isValidNameLengthCustomerPlanDTO(customerPlanDTO)) {
                    if (validation.isValidNumbersAndLengthCustomerPlanDTO(customerPlanDTO)) {
                        Customer customer = mapper.mapCustomerPlanDTOToCustomer(customerPlanDTO);
                        List<Plan> plans = customerPlanDTO.getPlanDTOS().stream()
                                .map(planDTO -> {
                                    Plan plan = mapper.mapPlanDTOToPlan(planDTO);
                                    return PlanRepository.save(plan);
                                })
                                .collect(Collectors.toList());

                        customer.setPlans(plans);
                        customerRepository.save(customer);

                        String message = "Ο πελάτης " + "'" + customer.getFirstName() + " " + customer.getLastName() + "'" + " με τα πλάνα γράφτηκαν επιτυχώς!";
                        CustomerResponseMessage response = new CustomerResponseMessage(message, customerPlanDTO);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                        CustomerResponseMessage response = new CustomerResponseMessage(message, customerPlanDTO);
                        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                    }
                }else {
                    String message = "Οι χαρακτήρες πρέπει να είναι γράμματα  '5-30'..";
                    CustomerResponseMessage response = new CustomerResponseMessage(message, null);
                    return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                }
            } else {
                String message = "Ο πελάτης με email" + "'" + customerPlanDTO.getEmail() + "'" + " υπάρχει ήδη..";
                CustomerResponseMessage response = new CustomerResponseMessage(message, null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Το email είναι υποχρεωτκό..";
        CustomerResponseMessage response = new CustomerResponseMessage(message, null);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> updateCustomer(Map<String, String> requestMap, int id) {
        try {
            Optional<Customer> existingCustomer = customerRepository.findById(id);
            if (existingCustomer.isPresent()) {
                Customer updateCustomer = existingCustomer.get();
                if (validation.isValidNameLengthCustomerDTO(requestMap)) {
                updateCustomer.setFirstName(requestMap.get("firstName"));
                updateCustomer.setLastName(requestMap.get("lastName"));
                }else {
                    String message = "Οι χαρακτήρες πρέπει να είναι γράμματα  '5-30'..";
                    ResponseMessage response = new ResponseMessage(message, (CustomerDTO) null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                if (!Objects.equals(updateCustomer.getEmail(), requestMap.get("email"))) {
                    updateCustomer.setEmail(requestMap.get("email"));
                }
                if (validation.isValidNumbersAndLengthCustomerDTO(requestMap)) {
                updateCustomer.setPhone(requestMap.get("phone"));
                } else {
                    String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                    ResponseMessage response = new ResponseMessage(message, (CustomerDTO) null);
                    return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                }
                updateCustomer.setAddress(requestMap.get("address"));
                updateCustomer.setBirthday(PlanUtils.formatter(requestMap));
                updateCustomer.setGender(Gender.valueOf(requestMap.get("gender")));
                customerRepository.save(updateCustomer);
            } else {
                String message = "Ο πελάτης ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, (CustomerDTO) null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
            String message = "Ο πελάτης " + "'" + requestMap.get("firstName") + " " + requestMap.get("lastName") + "'" + " ενημερώθηκε επιτυχώς!";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, (CustomerDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    @Transactional
    @Override
    public ResponseEntity<CustomerResponseMessage> deleteCustomer(int id) {
        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                customerRepository.delete(optionalCustomer.get());
                String message = "Ο πελάτης " + "'" + optionalCustomer.get().getFirstName() + " " + optionalCustomer.get().getLastName() + "'" + " διαγράφτηκε επιτυχώς!";
                CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                String message = "Ο πελάτης ΔΕΝ βρέθηκε..";
                CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error deleting customer", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @Transactional
    @Override
    public ResponseEntity<CustomerResponseMessage> deleteCustomerAndPlanById(int customerId, int PlanId) {
        try {
            Optional<Customer> existingCustomer = customerRepository.findById(customerId);
            Optional<Plan> existingPlan = PlanRepository.findById(PlanId);
            if (existingCustomer.isPresent()) {
                if (existingPlan.isPresent()) {
                    PlanRepository.deleteById(PlanId);
                    customerRepository.deleteById(customerId);
                }
                String message = "Ο πελάτης " + "'" + existingCustomer.get().getFirstName() + " " + existingCustomer.get().getLastName() + "'" + "και το πλάνο " + "'" + existingPlan.get().getName() + "'" + " διαγράφτηκε επιτυχώς!";
                CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerPlanDTO) null);
                return new ResponseEntity<> (response, HttpStatus.OK);

            } else {
                String message = "Ο πελάτης ή το πλάνο ΔΕΝ βρέθηκαν..";
                CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerPlanDTO) null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerPlanDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
