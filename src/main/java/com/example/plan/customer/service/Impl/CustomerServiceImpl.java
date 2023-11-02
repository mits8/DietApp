package com.example.plan.customer.service.Impl;

import com.example.plan.validation.Validation;
import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customer.service.CustomerService;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerWeeklyPlanDTO;
import com.example.plan.map.Mapper;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import com.example.plan.weeklyPlan.repository.WeeklyPlanRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WeeklyPlanRepository weeklyPlanRepository;


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
    public List<CustomerWeeklyPlanDTO> findCustomerWithWeeklyPlan() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerWeeklyPlanDTO> customerWeeklyPlanDTOS= customers.stream()
                .map(mapper::customerWeeklyPlanDTO)
                .collect(Collectors.toList());
        return customerWeeklyPlanDTOS;
    }


    @Override
    public CustomerDTO findById(int id) {
        Optional<Customer> optional = customerRepository.findById(id);
        if (optional.isPresent()) {
        Customer customer = optional.get();
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
    public ResponseEntity<CustomerResponseMessage> addCustomer(CustomerDTO customerDTO) {
        try {
            Optional<Customer> email = customerRepository.findByEmail(customerDTO.getEmail());
            if (Objects.isNull(email)) {
                if (validation.isValidNameLengthCustomerDTO(customerDTO)) {
                    if (validation.isValidNumbersAndLengthCustomerDTO(customerDTO)) {
                        Customer customer = mapper.mapCustomerDTOToCustomer(customerDTO);
                        customerRepository.save(customer);

                        String message = "Ο πελάτης " + "'" + customer.getFirstName() + " " + customer.getLastName() + "'" + " γράφτηκε επιτυχώς!";
                        CustomerResponseMessage response = new CustomerResponseMessage(message, customerDTO);
                        return new ResponseEntity<>(response, HttpStatus.CREATED);
                    } else {
                        String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                        CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
                        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                    }
                }else {
                    String message = "Οι χαρακτήρες πρέπει να είναι γράμματα '5-30'..";
                    CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
                    return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                }
            } else {
                String message = "Ο πελάτης με email" + "'" + customerDTO.getEmail() + "'" + " υπάρχει ήδη..";
                CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Τα βασικά στοιχεία είναι υποχρεωτκά..";
        CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<CustomerResponseMessage> addCustomerWithWeeklyPlan(CustomerWeeklyPlanDTO customerWeeklyPlanDTO) {
        try {

            Optional<Customer> email = customerRepository.findByEmail(customerWeeklyPlanDTO.getEmail());
            if (Objects.isNull(email)) {
                if (validation.isValidNameLengthCustomerWeeklyPlanDTO(customerWeeklyPlanDTO)) {
                    if (validation.isValidNumbersAndLengthCustomerWeeklyPlanDTO(customerWeeklyPlanDTO)) {
                        Customer customer = mapper.mapCustomerWeeklyPlanDTOToCustomer(customerWeeklyPlanDTO);
                        List<WeeklyPlan> weeklyPlanEntities = customerWeeklyPlanDTO.getPlans().stream()
                                .filter(weeklyPlanDTO -> weeklyPlanDTO.getId() == 0)
                                .map(weeklyPlanDTO -> {
                                    WeeklyPlan weeklyPlan = mapper.mapWeeklyPlanDTOToWeeklyPlan(weeklyPlanDTO);
                                    return weeklyPlanRepository.save(weeklyPlan);
                                })
                                .collect(Collectors.toList());

                        customer.setPlans(weeklyPlanEntities);
                        customerRepository.save(customer);

                        String message = "Ο πελάτης " + "'" + customer.getFirstName() + " " + customer.getLastName() + "'" + " με τα πλάνα γράφτηκαν επιτυχώς!";
                        CustomerResponseMessage response = new CustomerResponseMessage(message, customerWeeklyPlanDTO);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                        CustomerResponseMessage response = new CustomerResponseMessage(message, customerWeeklyPlanDTO);
                        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                    }
                }else {
                    String message = "Οι χαρακτήρες πρέπει να είναι γράμματα  '5-30'..";
                    CustomerResponseMessage response = new CustomerResponseMessage(message, null);
                    return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
                }
            } else {
                String message = "Ο πελάτης με email" + "'" + customerWeeklyPlanDTO.getEmail() + "'" + " υπάρχει ήδη..";
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
    public ResponseEntity<CustomerResponseMessage> updateCustomer(CustomerDTO customerDTO, int id) {
        try {
           Optional<Customer> existingCustomer = customerRepository.findById(id);
           if (existingCustomer.isPresent()) {
               Customer updateCustomer = existingCustomer.get();
               if (validation.isValidNameLengthCustomerDTO(customerDTO)) {
                   updateCustomer.setFirstName(customerDTO.getFirstName());
                   updateCustomer.setLastName(customerDTO.getLastName());
               }else {
                   String message = "Οι χαρακτήρες πρέπει να είναι γράμματα  '5-30'..";
                   CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
                   return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
               }
               if (!Objects.equals(updateCustomer.getEmail(), customerDTO.getEmail())) {
                   updateCustomer.setEmail(customerDTO.getEmail());
               }
               if (validation.isValidNumbersAndLengthCustomerDTO(customerDTO)) {
               updateCustomer.setPhone(customerDTO.getPhone());
               } else {
                   String message = "Το τηλέφωνο πρέπει να περιέχει 10 αριθμούς ..";
                   CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
                   return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
               }
               updateCustomer.setAddress(customerDTO.getAddress());
               updateCustomer.setBirthday(customerDTO.getBirthday());
               updateCustomer.setGender(customerDTO.getGender());
               customerRepository.save(updateCustomer);
           } else {
               String message = "Ο πελάτης ΔΕΝ βρέθηκε..";
               CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
               return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
           }
            String message = "Ο πελάτης " + "'" + customerDTO.getFirstName() + " " + customerDTO.getLastName() + "'" + " ενημερώθηκε επιτυχώς!";
            CustomerResponseMessage response = new CustomerResponseMessage(message, customerDTO);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerDTO) null);
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
    public ResponseEntity<CustomerResponseMessage> deleteCustomerAndWeeklyPlanById(int customerId, int weeklyPlanId) {
        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
            Optional<WeeklyPlan> optionalWeeklyPlan = weeklyPlanRepository.findById(weeklyPlanId);
            if (optionalCustomer.isPresent()) {
                if (optionalWeeklyPlan.isPresent()) {
                    weeklyPlanRepository.deleteById(weeklyPlanId);
                    customerRepository.deleteById(customerId);
                }
                String message = "Ο πελάτης " + "'" + optionalCustomer.get().getFirstName() + " " + optionalCustomer.get().getLastName() + "'" + "και το πλάνο " + "'" + optionalWeeklyPlan.get().getName() + "'" + " διαγράφτηκε επιτυχώς!";
                CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerWeeklyPlanDTO) null);
                return new ResponseEntity<> (response, HttpStatus.OK);

            } else {
                String message = "Ο πελάτης ή το πλάνο ΔΕΝ βρέθηκαν..";
                CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerWeeklyPlanDTO) null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        CustomerResponseMessage response = new CustomerResponseMessage(message, (CustomerWeeklyPlanDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
