package com.example.plan.weeklyPlan.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.dto.weeklyPlan.WeeklyPlanDTO;
import com.example.plan.dto.weeklyPlan.WeeklyPlanMealCustomerDTO;
import com.example.plan.map.Mapper;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.customer.CustomerResponseMessage;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import com.example.plan.weeklyPlan.repository.WeeklyPlanRepository;
import com.example.plan.weeklyPlan.service.WeeklyPlanService;
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
@Transactional
public class WeeklyPlanServiceImpl implements WeeklyPlanService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private WeeklyPlanRepository weeklyPlanRepository;

    private final Mapper mapper;

    public WeeklyPlanServiceImpl(Mapper mapper) {
        this.mapper = mapper;
    }




    @Override
    public List<WeeklyPlanMealCustomerDTO> findAll() {

        List<WeeklyPlan> weeklyPlans = weeklyPlanRepository.findAll();
        List<WeeklyPlanMealCustomerDTO> weeklyPlanMealDTOs = weeklyPlans.stream()
                .map(mapper::mapWeeklyPlanToMealDTO)
                .collect(Collectors.toList());

        return weeklyPlanMealDTOs;
    }


    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addWeeklyPlan(WeeklyPlan inputWeeklyPlan) {
        try {
            WeeklyPlan existingPlan = weeklyPlanRepository.findByName(inputWeeklyPlan.getName());
            if (Objects.isNull(existingPlan)) {

                boolean allCustomerEmailsNonNull = inputWeeklyPlan.getCustomers().stream()
                        .allMatch(customer -> customer.getEmail() != null);

                if (!allCustomerEmailsNonNull) {
                    String message = "Το email είναι υποχρεωτικό..";
                    ResponseMessage response = new ResponseMessage(message, (WeeklyPlanDTO) null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                if (!inputWeeklyPlan.getMeals().isEmpty()) {
                List<Meal> meals = inputWeeklyPlan.getMeals().stream()
                        .filter(meal -> meal.getId() == 0)
                        .map(meal -> mealRepository.save(meal))
                        .collect(Collectors.toList());
                inputWeeklyPlan.setMeals(meals);
                }
                if (!inputWeeklyPlan.getCustomers().isEmpty()) {
                    List<Customer> customers = inputWeeklyPlan.getCustomers().stream()
                            .filter(customer -> customer.getId() == 0)
                            .filter(customer -> {
                                Optional<Customer> existingCustomer = customerRepository.findByEmail(customer.getEmail());
                                if (existingCustomer.isPresent()) {
                                    throw new RuntimeException("To email " + existingCustomer.get().getEmail() + " υπάρχει ήδη..");
                                }
                                return true;
                            })
                            .map(customer -> customerRepository.save(customer))
                            .collect(Collectors.toList());
                    inputWeeklyPlan.setCustomers(customers);
                }
                weeklyPlanRepository.save(inputWeeklyPlan);

                String message = "Το πλάνο " +  inputWeeklyPlan.getName() +  " γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message,  inputWeeklyPlan);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                String message = "Το πλάνο " + "'" + existingPlan.getName() + "'" + " υπάρχει ήδη..";
                ResponseMessage response = new ResponseMessage(message, (WeeklyPlanDTO) null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error while saving WeeklyPlan: {}", ex);
            String message = PlanConstants.SOMETHING_WENT_WRONG;
            ResponseMessage response = new ResponseMessage(message, (WeeklyPlanDTO) null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> updateWeeklyPlan(WeeklyPlanDTO weeklyPlanDTO, int id) {
        try {
            Optional<WeeklyPlan> optionalWeeklyPlan = weeklyPlanRepository.findById(id);
            if (optionalWeeklyPlan.isPresent()) {
                WeeklyPlan updateWeeklyPlan = optionalWeeklyPlan.get();
                updateWeeklyPlan.setId(weeklyPlanDTO.getId());
                updateWeeklyPlan.setName(weeklyPlanDTO.getName());
                updateWeeklyPlan.setDuration(weeklyPlanDTO.getDuration());
                updateWeeklyPlan.setStartDate(weeklyPlanDTO.getStartDate());
                updateWeeklyPlan.setEndDate(weeklyPlanDTO.getEndDate());
                weeklyPlanRepository.save(updateWeeklyPlan);
                String message = "Το πλάνο " + "'" + weeklyPlanDTO.getName() + " ενημερώθηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message,  weeklyPlanDTO);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, weeklyPlanDTO);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, (WeeklyPlanDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> deleteWeeklyPlan(int id){
        try {
            WeeklyPlan optionalWeeklyPlan =  weeklyPlanRepository.findWeeklyPlanById(id);
            if (!optionalWeeklyPlan.equals(null)){
                weeklyPlanRepository.delete(optionalWeeklyPlan);
                String message = "Το πλάνο " + "'" + optionalWeeklyPlan.getName() + " διαγράφηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, (WeeklyPlanDTO) null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, (WeeklyPlanDTO) null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, (WeeklyPlanDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<CustomerResponseMessage> removeCustomerFromWeeklyPlan(int customerId, int weeklyPlanId) {
        try {
            Optional<Customer> customer = customerRepository.findById(customerId);
            Optional<WeeklyPlan> weeklyPlan = weeklyPlanRepository.findById(weeklyPlanId);

            if (customer.isPresent() && weeklyPlan.isPresent()) {
                weeklyPlan.get().getCustomers().remove(customer.get());
                weeklyPlanRepository.save(weeklyPlan.get());
                String message = "Το πλάνο " + "'" + weeklyPlan.get().getName() + " αφαιρέθηκε επιτυχώς!";
                CustomerResponseMessage response = new CustomerResponseMessage(message,  null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
            String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                CustomerResponseMessage response = new CustomerResponseMessage(message, null);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        }  catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        CustomerResponseMessage response = new CustomerResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}