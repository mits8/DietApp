package com.example.plan.customer.service.Impl;

import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customer.service.CustomerService;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerPlanDTO;
import com.example.plan.map.Mapper;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.repository.PlanRepository;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.validation.Validation;
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
    public ResponseEntity<CustomerResponseMessage> addCustomer(CustomerDTO customerDTO) {
        try {
            Customer email = customerRepository.findByEmail(customerDTO.getEmail());
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
    public ResponseEntity<CustomerResponseMessage> deleteCustomerAndPlanById(int customerId, int PlanId) {
        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
            Optional<Plan> optionalPlan = PlanRepository.findById(PlanId);
            if (optionalCustomer.isPresent()) {
                if (optionalPlan.isPresent()) {
                    PlanRepository.deleteById(PlanId);
                    customerRepository.deleteById(customerId);
                }
                String message = "Ο πελάτης " + "'" + optionalCustomer.get().getFirstName() + " " + optionalCustomer.get().getLastName() + "'" + "και το πλάνο " + "'" + optionalPlan.get().getName() + "'" + " διαγράφτηκε επιτυχώς!";
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
