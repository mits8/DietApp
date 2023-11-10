package com.example.plan.plan.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.dto.Plan.PlanDTO;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.plan.PlanMealCustomerDTO;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.map.Mapper;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.repository.PlanRepository;
import com.example.plan.plan.service.PlanService;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.customer.CustomerResponseMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private PlanRepository planRepository;

    private final Mapper mapper;

    public PlanServiceImpl(Mapper mapper) {
        this.mapper = mapper;
    }




    @Override
    public List<PlanMealCustomerDTO> findAll() {

        List<Plan> Plans = planRepository.findAll();
        List<PlanMealCustomerDTO> PlanMealDTOs = Plans.stream()
                .map(mapper::mapPlanToMealDTO)
                .collect(Collectors.toList());

        return PlanMealDTOs;
    }


    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addPlan(Plan inputPlan) {
        try {
            Plan existingPlan = planRepository.findByName(inputPlan.getName());
            if (Objects.isNull(existingPlan)) {

                boolean allCustomerEmailsNonNull = inputPlan.getCustomers().stream()
                        .allMatch(customer -> customer.getEmail() != null);

                if (!allCustomerEmailsNonNull) {
                    String message = "Το email είναι υποχρεωτικό..";
                    ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                if (!inputPlan.getMeals().isEmpty()) {
                    List<Meal> meals = inputPlan.getMeals().stream()
                            .filter(meal -> meal.getId() == 0)
                            .map(meal -> {
                                List<Food> foods = new ArrayList<>();
                                for (Food food : meal.getFoods()) {
                                    if (food.getId() == 0) {
                                        foods.add(foodRepository.save(food));
                                    } else {
                                        Food existingFood = foodRepository.findById(food.getId()).orElse(null);
                                        if (existingFood != null) {
                                            foods.add(existingFood);
                                        } else {
                                            throw new RuntimeException("Το φαγητό με id " + food.getId() + " δεν βρέθηκε.");
                                        }
                                    }
                                }
                                meal.setFoods(foods);
                                return mealRepository.save(meal);
                            })
                            .collect(Collectors.toList());
                inputPlan.setMeals(meals);
                }
                if (!inputPlan.getCustomers().isEmpty()) {
                    List<Customer> customers = inputPlan.getCustomers().stream()
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
                    inputPlan.setCustomers(customers);
                }
                planRepository.save(inputPlan);

                String message = "Το πλάνο " +  inputPlan.getName() +  " γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, (Map<String, String>) null);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                String message = "Το πλάνο " + "'" + existingPlan.getName() + "'" + " υπάρχει ήδη..";
                ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error while saving Plan: {}", ex);
            String message = PlanConstants.SOMETHING_WENT_WRONG;
            ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addMealToPlan(Map<String, List<MealDTO>> mealData, int id) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(id);
            Plan plan;
            if (existingPlan.isPresent()) {
                plan = existingPlan.get();
                List<MealDTO> meals = mealData.get("mealDTOS");
                for (MealDTO mealDTO : meals) {
                    Meal meal = mapper.mapMealDTOToMeal(mealDTO);
                    plan.getMeals().add(meal);
                }
                planRepository.save(plan);

                PlanDTO planDTO = mapper.PlanToPlanDTO(plan);
                String message = "Το πλάνο γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, planDTO);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = PlanConstants.SOMETHING_WENT_WRONG;
        ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addCustomerToPlan(Map<String, List<CustomerDTO>> requestMap, int id) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(id);
            Plan plan;
            if (existingPlan.isPresent()) {
                plan = existingPlan.get();
                List<CustomerDTO> customerDTOS = requestMap.get("customerDTOS");
                for (CustomerDTO customerDTO : customerDTOS) {
                    Customer customer = mapper.mapCustomerDTOToCustomer(customerDTO);
                    plan.getCustomers().add(customer);
                }
                planRepository.save(plan);

                PlanDTO planDTO = mapper.PlanToPlanDTO(plan);
                String message = "Το πλάνο γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, planDTO);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = PlanConstants.SOMETHING_WENT_WRONG;
        ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> updatePlan(PlanDTO planDTO, int id) {
        try {
            Optional<Plan> optionalPlan = planRepository.findById(id);
            if (optionalPlan.isPresent()) {
                Plan updatePlan = optionalPlan.get();
                updatePlan.setId(planDTO.getId());
                updatePlan.setName(planDTO.getName());
                updatePlan.setDuration(planDTO.getDuration());
                updatePlan.setStartDate(planDTO.getStartDate());
                updatePlan.setEndDate(planDTO.getEndDate());
                planRepository.save(updatePlan);
                String message = "Το πλάνο " + "'" + planDTO.getName() + " ενημερώθηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message,  planDTO);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> deletePlan(int id){
        try {
            Plan optionalPlan =  planRepository.findPlanById(id);
            if (!optionalPlan.equals(null)){
                planRepository.delete(optionalPlan);
                String message = "Το πλάνο " + "'" + optionalPlan.getName() + " διαγράφηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, (PlanDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<CustomerResponseMessage> removeCustomerFromPlan(int customerId, int PlanId) {
        try {
            Optional<Customer> existingCustomer = customerRepository.findById(customerId);
            Optional<Plan> existingPlan = planRepository.findById(PlanId);

            if (existingCustomer.isPresent() && existingPlan.isPresent()) {
                existingPlan.get().getCustomers().remove(existingCustomer.get());
                planRepository.save(existingPlan.get());
                customerRepository.deleteById(customerId);
                String message = "Ο πελάτης " + "'" + existingCustomer.get().getFirstName() + " " + existingCustomer.get().getLastName() + "'" + " αφαιρέθηκε επιτυχώς από το πλάνο " + "'" + existingPlan.get().getName() + "'";
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