package com.example.plan.customer.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.customer.service.CustomerService;
import com.example.plan.meal.entity.Meal;
import com.example.plan.utils.PlanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findById(int id) {
        Optional<Customer> optional = customerRepository.findById(id);
        Customer customer = null;
        if (optional.isPresent()) {
            customer = optional.get();
        } else {
            throw new RuntimeException("Ο πελάτης " + customer.getFirstName() + "\t" + customer.getLastName() + " δεν βρέθηκε..");
        }
        return customer;
    }

    @Override
    public ResponseEntity<String> save(Customer customer) {
        try {
            int lengthFirstName = customer.getFirstName().length();
            int lengthLastName = customer.getLastName().length();
            Customer email = customerRepository.findByEmail(customer.getEmail());
            if (Objects.isNull(email)) {
                if ((lengthFirstName >= 5 && lengthFirstName < 30) && (lengthLastName >= 5 && lengthLastName < 30)) {
                    customer.setMeals(customer.getMeals());
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
    public ResponseEntity<String> updateCustomer(Customer customer, int id) {
        try {
           Optional<Customer> existingCustomer = customerRepository.findById(id);
           if (existingCustomer.isPresent()) {
               Customer updateCustomer = existingCustomer.get();
               updateCustomer.setFirstName(customer.getFirstName());
               updateCustomer.setLastName(customer.getLastName());
               updateCustomer.setEmail(customer.getEmail());
               updateCustomer.setPhone(customer.getPhone());
               updateCustomer.setCity(customer.getCity());
               updateCustomer.setAddress(customer.getAddress());
               updateCustomer.setBirthday(customer.getBirthday());
               updateCustomer.setGender(customer.getGender());
               customerRepository.save(updateCustomer);
           } else {
               return new ResponseEntity<>( "Ο πελάτης ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
           }
            return new ResponseEntity<>("Ο πελάτης " + "\"" + customer.getFirstName() + " " + customer.getLastName() + "\"" + " ενημερώθηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCustomer(int id) {
        try {
            Optional<Customer> customer = customerRepository.findById(id);
            if (customer.isPresent()) {
                customerRepository.deleteById(id);
            } else {
                return new ResponseEntity<>("Ο πελάτης ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<> ("Ο πελάτης " + "\"" + customer.get().getFirstName() + " " + customer.get().getLastName() + "\"" + " διαγράφτηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
