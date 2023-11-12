package com.example.plan.plan.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.dto.plan.PlanMealCustomerDTO;
import com.example.plan.enums.Day;
import com.example.plan.enums.Duration;
import com.example.plan.enums.Gender;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.map.Mapper;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.repository.PlanRepository;
import com.example.plan.plan.service.PlanService;
import com.example.plan.utils.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    ObjectMapper objectMapper = new ObjectMapper();

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

    @Override
    public List<Object> getPlanDetailsByCustomerFirstName(String customerFirstName) {
        return planRepository.findPlanDetailsByCustomerFirstName(customerFirstName);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addToPlan(Map<String, List<Object>> requestMap, int id) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(id);

            if (existingPlan.isPresent()) {
                Plan plan = existingPlan.get();
                addMealsAndFoodsToPlan(plan, requestMap);
            } else {
                List<Object> plans = requestMap.get("plans");
                for (Object objectPlan : plans) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> planData = objectMapper.convertValue(objectPlan, Map.class);
                    String name = String.valueOf(planData.get("name"));
                    LocalDate startDate = LocalDate.parse(String.valueOf(planData.get("startDate")));
                    LocalDate endDate = LocalDate.parse(String.valueOf(planData.get("endDate")));
                    Duration duration = Duration.parse(String.valueOf(planData.get("duration")));
                    Plan plan = new Plan();
                    plan.setName(name);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDuration(duration);
                    addMealsAndFoodsToPlan(plan, requestMap);
                    planRepository.save(plan);
                }
            }

                String message = "Το πλάνο γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = PlanConstants.SOMETHING_WENT_WRONG;
        ResponseMessage response = new ResponseMessage(message,  null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addMealsAndFoodsToPlan(Plan plan, Map<String, List<Object>> requestMap) {
        List<Object> meals = requestMap.get("meals");
        if (meals != null) {
        for (Object mealObject : meals) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> mealData = objectMapper.convertValue(mealObject, Map.class);

            String mealName = String.valueOf(mealData.get("name"));
            String mealDescr = String.valueOf(mealData.get("description"));
            Day day = Day.valueOf((String) mealData.get("day"));
            Type mealType = Type.valueOf((String) mealData.get("type"));
            String quantity = String.valueOf(mealData.get("quantity"));
            Meal meal = mealRepository.findByName(mealName);

            if (meal == null) {
                meal = new Meal();
                meal.setName(mealName);
                meal.setDescription(mealDescr);
                meal.setDay(day);
                meal.setType(mealType);
                meal.setQuantity(quantity);
            }
            List<Object> foods = (List<Object>) mealData.get("foods");
            if (foods != null) {
                for (Object foodObject : foods) {
                    Map<String, String> foodData = objectMapper.convertValue(foodObject, Map.class);

                    String foodName = String.valueOf(foodData.get("name"));
                    String foodDescr = String.valueOf(mealData.get("description"));
                    Double gram = Double.valueOf(String.valueOf(foodData.get("gram")));
                    Double calories = Double.valueOf(String.valueOf(foodData.get("calories")));
                    Type foodType = Type.valueOf(foodData.get("type"));

                    Food food = foodRepository.findByName(foodName);

                    if (food == null) {
                        food = new Food();
                        food.setName(foodName);
                        food.setDescription(foodDescr);
                        food.setGram(gram);
                        food.setCalories(calories);
                        food.setType(foodType);
                        foodRepository.save(food);
                    }

                    meal.getFoods().add(food);
                }
            }
            plan.getMeals().add(meal);
        }
        }
        planRepository.save(plan);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addMealToPlan(Map<String, List<Object>> requestMap, int id) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(id);
            Plan plan;
            if (existingPlan.isPresent()) {
                plan = existingPlan.get();
                addMeals(plan, requestMap);
            } else {
                List<Object> plans = requestMap.get("plans");
                for (Object objectPlan : plans) {

                    Map<String, Object> planData = objectMapper.convertValue(objectPlan, Map.class);
                    String name = String.valueOf(planData.get("name"));
                    LocalDate startDate = LocalDate.parse(String.valueOf(planData.get("startDate")));
                    LocalDate endDate = LocalDate.parse(String.valueOf(planData.get("endDate")));
                    Duration duration = Duration.parse(String.valueOf(planData.get("duration")));
                    plan = new Plan();
                    plan.setName(name);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDuration(duration);
                    addMeals(plan, requestMap);
                    planRepository.save(plan);

                }
            }
                String message = "Το πλάνο γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = PlanConstants.SOMETHING_WENT_WRONG;
        ResponseMessage response = new ResponseMessage(message,  null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addMeals(Plan plan, Map<String, List<Object>> requestMap) {
        List<Object> meals = requestMap.get("meals");
        if (meals != null) {
            for (Object mealObject : meals) {

                Map<String, Object> mealData = objectMapper.convertValue(mealObject, Map.class);

                String mealName = String.valueOf(mealData.get("name"));
                String mealDescr = String.valueOf(mealData.get("description"));
                Day day = Day.valueOf((String) mealData.get("day"));
                Type mealType = Type.valueOf((String) mealData.get("type"));
                String quantity = String.valueOf(mealData.get("quantity"));
                Meal meal = mealRepository.findByName(mealName);

                if (meal == null) {
                    meal = new Meal();
                    meal.setName(mealName);
                    meal.setDescription(mealDescr);
                    meal.setDay(day);
                    meal.setType(mealType);
                    meal.setQuantity(quantity);
                }
                List<Object> foods = (List<Object>) mealData.get("foods");
                if (foods != null) {
                    for (Object foodObject : foods) {
                        Map<String, String> foodData = objectMapper.convertValue(foodObject, Map.class);

                        String foodName = String.valueOf(foodData.get("name"));
                        String foodDescr = String.valueOf(mealData.get("description"));
                        Double gram = Double.valueOf(String.valueOf(foodData.get("gram")));
                        Double calories = Double.valueOf(String.valueOf(foodData.get("calories")));
                        Type foodType = Type.valueOf(foodData.get("type"));
                        Food food = foodRepository.findByName(foodName);

                        if (food == null) {
                            food = new Food();
                            food.setName(foodName);
                            food.setDescription(foodDescr);
                            food.setGram(gram);
                            food.setCalories(calories);
                            food.setType(foodType);
                            foodRepository.save(food);
                        }

                        meal.getFoods().add(food);
                    }

                    plan.getMeals().add(meal);
                }
            }
            planRepository.save(plan);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addCustomerToPlan(Map<String, List<Object>> requestMap, int id) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(id);
            Plan plan;
            if (existingPlan.isPresent()) {
                plan = existingPlan.get();
                addCustomer(plan, requestMap);
            } else {
                List<Object> plans = requestMap.get("plans");
                for (Object objectPlan : plans) {

                    Map<String, Object> planData = objectMapper.convertValue(objectPlan, Map.class);
                    String name = String.valueOf(planData.get("name"));
                    LocalDate startDate = LocalDate.parse(String.valueOf(planData.get("startDate")));
                    LocalDate endDate = LocalDate.parse(String.valueOf(planData.get("endDate")));
                    Duration duration = Duration.parse(String.valueOf(planData.get("duration")));
                    plan = new Plan();
                    plan.setName(name);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDuration(duration);
                    addCustomer(plan, requestMap);
                    planRepository.save(plan);
                }
            }

                String message = "Το πλάνο γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = PlanConstants.SOMETHING_WENT_WRONG;
        ResponseMessage response = new ResponseMessage(message,  null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addCustomer(Plan plan, Map<String, List<Object>> requestMap) {
        List<Object> customers = requestMap.get("customers");
        if (customers != null) {
            for (Object customerObject : customers) {

                Map<String, Object> customerData = objectMapper.convertValue(customerObject, Map.class);

                String customerFirstname = String.valueOf(customerData.get("firstName"));
                String customerLastname = String.valueOf(customerData.get("lastName"));
                String email = String.valueOf(customerData.get("email"));
                String phone = String.valueOf(customerData.get("phone"));
                String city = String.valueOf(customerData.get("city"));
                String address = String.valueOf(customerData.get("address"));
                LocalDate birthday = LocalDate.parse(String.valueOf(customerData.get("birthday")));
                Gender gender = Gender.valueOf((String) customerData.get("gender"));

                Customer customer = new Customer();
                customer.setFirstName(customerFirstname);
                customer.setLastName(customerLastname);
                customer.setEmail(email);
                customer.setPhone(phone);
                customer.setCity(city);
                customer.setAddress(address);
                customer.setBirthday(birthday);
                customer.setGender(gender);
                customerRepository.save(customer);
                plan.getCustomers().add(customer);
            }
        }
        planRepository.save(plan);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> updatePlan(Map<String, String> requestMap, int id) {
        try {
            Optional<Plan> optionalPlan = planRepository.findById(id);
            if (optionalPlan.isPresent()) {
                Plan updatePlan = optionalPlan.get();
                updatePlan.setName(requestMap.get("name"));
                updatePlan.setDuration(Duration.parse(requestMap.get("duration")));
                updatePlan.setStartDate(LocalDate.parse(requestMap.get("startDate")));
                updatePlan.setEndDate(LocalDate.parse(requestMap.get("endDate")));
                planRepository.save(updatePlan);
                String message = "Το πλάνο " + "'" + requestMap.get("name") + " ενημερώθηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message,  requestMap);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
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
                ResponseMessage response = new ResponseMessage(message,  null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message,  null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> removeCustomerFromPlan(int customerId, int PlanId) {
        try {
            Optional<Customer> existingCustomer = customerRepository.findById(customerId);
            Optional<Plan> existingPlan = planRepository.findById(PlanId);

            if (existingCustomer.isPresent() && existingPlan.isPresent()) {
                existingPlan.get().getCustomers().remove(existingCustomer.get());
                planRepository.save(existingPlan.get());
                customerRepository.deleteById(customerId);
                String message = "Ο πελάτης " + "'" + existingCustomer.get().getFirstName() + " " + existingCustomer.get().getLastName() + "'" + " αφαιρέθηκε επιτυχώς από το πλάνο " + "'" + existingPlan.get().getName() + "'";
                ResponseMessage response = new ResponseMessage(message,  null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
            String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        }  catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}