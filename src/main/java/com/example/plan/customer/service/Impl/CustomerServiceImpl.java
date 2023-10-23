package com.example.plan.customer.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.dto.CustomerDTO;
import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customer.service.CustomerService;
import com.example.plan.dto.CustomerWeeklyPlanDTO;
import com.example.plan.map.Mapper;
import com.example.plan.utils.PlanUtils;
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
    public ResponseEntity<String> save(Customer customer) {
        try {
            int lengthFirstName = customer.getFirstName().length();
            int lengthLastName = customer.getLastName().length();
            Customer email = customerRepository.findByEmail(customer.getEmail());
            if (Objects.isNull(email)) {
                if ((lengthFirstName >= 5 && lengthFirstName < 30) && (lengthLastName >= 5 && lengthLastName < 30)) {
                    customerRepository.save(customer);
                }else {
                    return new ResponseEntity<>("Το μήκος πρέπει να είναι ανάμεσα '5-30..", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Ο πελάτης με email" + customer.getEmail() + " υπάρχει ήδη..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Ο πελάτης " + "\"" + customer.getFirstName() + " " + customer.getLastName() + "\"" + " γράφτηκε επιτυχώς!", HttpStatus.OK);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return new ResponseEntity<>("Το email είναι υποχρεωτκό..", HttpStatus.BAD_REQUEST);
        }

    @Override
    public ResponseEntity<String> saveCustomerWithWeeklyPlan(Customer customer) {
        try {
            int lengthFirstName = customer.getFirstName().length();
            int lengthLastName = customer.getLastName().length();
            Customer email = customerRepository.findByEmail(customer.getEmail());
            if (Objects.isNull(email)) {
                if ((lengthFirstName >= 5 && lengthFirstName < 30) && (lengthLastName >= 5 && lengthLastName < 30)) {
                    List<WeeklyPlan> weeklyPlans = customer.getPlans().stream()
                                    .filter(weeklyPlan -> weeklyPlan.getId() == 0)
                                    .map(weeklyPlan -> weeklyPlanRepository.save(weeklyPlan))
                                    .collect(Collectors.toList());
                    customer.setPlans(weeklyPlans);
                    customerRepository.save(customer);
                }else {
                    return new ResponseEntity<>("Το μήκος πρέπει να είναι ανάμεσα '5-30..", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Ο πελάτης με email" + customer.getEmail() + " υπάρχει ήδη..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Ο πελάτης " + "\"" + customer.getFirstName() + " " + customer.getLastName() + "\"" + " γράφτηκε επιτυχώς!", HttpStatus.OK);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return new ResponseEntity<>("Το email είναι υποχρεωτκό..", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> updateCustomer(CustomerDTO customerDTO, int id) {
        try {
           Optional<Customer> existingCustomer = customerRepository.findById(id);
           if (existingCustomer.isPresent()) {
               Customer updateCustomer = existingCustomer.get();
               updateCustomer.setFirstName(customerDTO.getFirstName());
               updateCustomer.setLastName(customerDTO.getLastName());
               if (!Objects.equals(updateCustomer.getEmail(), customerDTO.getEmail())) {
                   updateCustomer.setEmail(customerDTO.getEmail());
               } else {
                   return new ResponseEntity<>("Το email υπάρχη ήδη..", HttpStatus.BAD_REQUEST);
               }
               updateCustomer.setPhone(customerDTO.getPhone());
               updateCustomer.setAddress(customerDTO.getAddress());
               updateCustomer.setBirthday(customerDTO.getBirthday());
               updateCustomer.setGender(customerDTO.getGender());
               customerRepository.save(updateCustomer);
           } else {
               return new ResponseEntity<>( "Ο πελάτης ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
           }
            return new ResponseEntity<>("Ο πελάτης " + "\"" + customerDTO.getFirstName() + " " + customerDTO.getLastName() + "\"" + " ενημερώθηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCustomer(int id) {
        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {

                customerRepository.deleteById(id);
            } else {
                return new ResponseEntity<>("Ο πελάτης ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<> ("Ο πελάτης " + "\"" + optionalCustomer.get().getFirstName() + " " + optionalCustomer.get().getLastName() + "\"" + " διαγράφτηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteCustomerAndWeeklyPlanById(int customerId, int weeklyPlanId) {
        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
            Optional<WeeklyPlan> optionalWeeklyPlan = weeklyPlanRepository.findById(weeklyPlanId);
            if (optionalCustomer.isPresent()) {
                weeklyPlanRepository.deleteById(weeklyPlanId);
                customerRepository.deleteById(customerId);
            } else {
                return new ResponseEntity<>("Ο πελάτης ή το πλάνο ΔΕΝ βρέθηκαν..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<> ("Ο πελάτης " + "\"" + optionalCustomer.get().getFirstName() + " " + optionalCustomer.get().getLastName() + "\"" + "και το πλάνο " + "\"" + optionalWeeklyPlan.get().getName() + "\"" + " διαγράφτηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
