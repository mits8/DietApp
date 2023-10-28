package com.example.plan.weeklyPlan.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.dto.weeklyPlan.WeeklyPlanMealCustomerDTO;
import com.example.plan.map.Mapper;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.utils.ResponseMessage;
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



    @Override
    public ResponseEntity<ResponseMessage> save(WeeklyPlan inputWeeklyPlan) {
        try {
            WeeklyPlan existingPlan = weeklyPlanRepository.findByName(inputWeeklyPlan.getName());

            if (Objects.isNull(existingPlan)) {

                boolean allCustomerEmailsNonNull = inputWeeklyPlan.getCustomers().stream()
                        .allMatch(customer -> customer.getEmail() != null);

                if (!allCustomerEmailsNonNull) {
                    String message = "Το email είναι υποχρεωτικό..";
                    ResponseMessage response = new ResponseMessage(message, (WeeklyPlan) null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                /*if (!inputWeeklyPlan.getMeals().isEmpty()) {
                List<Meal> meals = inputWeeklyPlan.getMeals().stream()
                        .filter(meal -> meal.getId() == 0)
                        .map(meal -> mealRepository.save(meal))
                        .collect(Collectors.toList());
                inputWeeklyPlan.setMeals(meals);
                }*/
                if (!inputWeeklyPlan.getCustomers().isEmpty()) {
                    List<Customer> customers = inputWeeklyPlan.getCustomers().stream()
                            .filter(customer -> customer.getId() == 0)
                            .filter(customer -> {
                                Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());
                                if (!Objects.isNull(existingCustomer)) {
                                    throw new RuntimeException("To email " + existingCustomer.getEmail() + " υπάρχει ήδη..");
                                }
                                return true;
                            })
                            .map(customer -> customerRepository.save(customer))
                            .collect(Collectors.toList());
                    inputWeeklyPlan.setCustomers(customers);
                }
                weeklyPlanRepository.save(inputWeeklyPlan);

                String message = "Το πλάνο " +  inputWeeklyPlan.getName() +  " γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, inputWeeklyPlan);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                String message = "Το πλάνο " + "'" + existingPlan.getName() + "'" + " υπάρχει ήδη..";
                ResponseMessage response = new ResponseMessage(message, (WeeklyPlan) null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error while saving WeeklyPlan: {}", ex);
            String message = PlanConstants.SOMETHING_WENT_WRONG;
            ResponseMessage response = new ResponseMessage(message, (WeeklyPlan) null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}