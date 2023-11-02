package com.example.plan.map;

import com.example.plan.customer.entity.Customer;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerWeeklyPlanDTO;
import com.example.plan.dto.food.FoodDTO;
import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.dto.meal.MealFoodWeeklyPlanDTO;
import com.example.plan.dto.weeklyPlan.WeeklyPlanDTO;
import com.example.plan.dto.weeklyPlan.WeeklyPlanMealCustomerDTO;
import com.example.plan.food.entity.Food;
import com.example.plan.meal.entity.Meal;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Mapper {

    private Customer customer = new Customer();
    private CustomerDTO customerDTO = new CustomerDTO();
    private CustomerWeeklyPlanDTO customerWeeklyPlanDTO = new CustomerWeeklyPlanDTO();

    private Food food = new Food();
    private FoodDTO foodDTO = new FoodDTO();

    private Meal meal = new Meal();
    private MealDTO mealDTO = new MealDTO();
    private MealFoodDTO mealFoodDTO = new MealFoodDTO();

    private WeeklyPlan weeklyPlan = new WeeklyPlan();
    private WeeklyPlanDTO weeklyPlanDTO = new WeeklyPlanDTO();
    private MealFoodWeeklyPlanDTO mealFoodWeeklyPlanDTO = new MealFoodWeeklyPlanDTO();
    private WeeklyPlanMealCustomerDTO weeklyPlanMealCustomerDTO = new WeeklyPlanMealCustomerDTO();

    public WeeklyPlanMealCustomerDTO mapWeeklyPlanToMealDTO(WeeklyPlan weeklyPlan) {
        weeklyPlanMealCustomerDTO.setId(weeklyPlan.getId());
        weeklyPlanMealCustomerDTO.setName(weeklyPlan.getName());
        weeklyPlanMealCustomerDTO.setStartDate(weeklyPlan.getStartDate());
        weeklyPlanMealCustomerDTO.setEndDate(weeklyPlan.getEndDate());

        if (!Objects.isNull(weeklyPlan.getMeals())) {
            List<MealDTO> mealDTOs = weeklyPlan.getMeals().stream()
                    .map(this::mapMealToMealDTO)
                    .collect(Collectors.toList());
            weeklyPlanMealCustomerDTO.setMeals(mealDTOs);
        }
        List<CustomerDTO> customerDTOs = weeklyPlan.getCustomers().stream()
                .map(this::mapCustomerToCustomerDTO)
                .collect(Collectors.toList());
        weeklyPlanMealCustomerDTO.setCustomers(customerDTOs);
        return weeklyPlanMealCustomerDTO;

    }

    public MealFoodDTO mapMealToMealFoodDTO(Meal meal) {
        mealFoodDTO.setName(meal.getName());
        mealFoodDTO.setDescription(meal.getDescription());
        mealFoodDTO.setType(meal.getType());
        mealFoodDTO.setDay(meal.getDay());
        if(!Objects.isNull(meal.getFoods())) {
            List<FoodDTO> foodDTOS = meal.getFoods().stream()
                    .map(this::mapFoodToFoodDTO)
                    .collect(Collectors.toList());
            mealFoodDTO.setFoodDTOS(foodDTOS);
        }
        return mealFoodDTO;
    }

    public MealFoodWeeklyPlanDTO mapMealToMealFoodWeeklyPlanDTO(Meal meal) {
        mealFoodWeeklyPlanDTO.setName(meal.getName());
        mealFoodWeeklyPlanDTO.setDescription(meal.getDescription());
        mealFoodWeeklyPlanDTO.setType(meal.getType());
        mealFoodWeeklyPlanDTO.setDay(meal.getDay());
        return mealFoodWeeklyPlanDTO;
    }

    public CustomerWeeklyPlanDTO customerWeeklyPlanDTO(Customer customer) {
        customerWeeklyPlanDTO.setId(customer.getId());
        customerWeeklyPlanDTO.setFirstName(customer.getFirstName());
        customerWeeklyPlanDTO.setLastName(customer.getLastName());
        customerWeeklyPlanDTO.setEmail(customer.getEmail());
        customerWeeklyPlanDTO.setPhone(customer.getPhone());

        List<WeeklyPlanDTO> weeklyPlanDTOS = customer.getPlans().stream()
                .map(this::weeklyPlanToWeeklyPlanDTO)
                .collect(Collectors.toList());
        customerWeeklyPlanDTO.setPlans(weeklyPlanDTOS);
        return customerWeeklyPlanDTO;
    }

    public MealDTO mapMealToMealDTO(Meal meal) {
        mealDTO.setId(meal.getId());
        mealDTO.setName(meal.getName());
        mealDTO.setDescription(meal.getDescription());
        mealDTO.setDay(meal.getDay());
        mealDTO.setType(meal.getType());
        return mealDTO;
    }

    public Meal mapMealDTOToMeal(MealDTO mealDTO) {
        mapMealCommonProperties(meal, mealDTO);
        return meal;
    }

    public Meal mapMealFoodDTOToMeal(MealFoodDTO mealFoodDTO) {
        mapMealCommonProperties(meal, mealFoodDTO);
        return meal;
    }

    public Food mapMealFoodDTOToFood(MealFoodDTO mealFoodDTO){
        food.setName(mealFoodDTO.getName());
        food.setDescription(mealFoodDTO.getDescription());
        food.setType(mealFoodDTO.getType());
        return food;
    }

    public CustomerDTO mapCustomerToCustomerDTO(Customer customer){
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setBirthday(customer.getBirthday());
        customerDTO.setGender(customer.getGender());
        return customerDTO;
    }

    public Customer mapCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        mapCustomerCommonProperties(customer, customerDTO);
        return customer;
    }

    public Customer mapCustomerWeeklyPlanDTOToCustomer(CustomerWeeklyPlanDTO customerWeeklyPlanDTO) {
        Customer customer = new Customer();
        mapCustomerCommonProperties(customer, customerWeeklyPlanDTO);
        return customer;
    }


    public WeeklyPlanDTO weeklyPlanToWeeklyPlanDTO(WeeklyPlan weeklyPlan) {
        weeklyPlanDTO.setId(weeklyPlan.getId());
        weeklyPlanDTO.setName(weeklyPlan.getName());
        weeklyPlanDTO.setStartDate(weeklyPlan.getStartDate());
        weeklyPlanDTO.setEndDate(weeklyPlan.getEndDate());
        return weeklyPlanDTO;
    }

    public WeeklyPlan mapWeeklyPlanDTOToWeeklyPlan(WeeklyPlanDTO weeklyPlanDTO) {
        weeklyPlan.setId(weeklyPlanDTO.getId());
        weeklyPlan.setName(weeklyPlanDTO.getName());
        weeklyPlan.setDuration(weeklyPlanDTO.getDuration());
        weeklyPlan.setStartDate(weeklyPlanDTO.getStartDate());
        weeklyPlan.setEndDate(weeklyPlanDTO.getEndDate());
        return weeklyPlan;
    }

    public FoodDTO mapFoodToFoodDTO(Food food) {
        foodDTO.setName(food.getName());
        foodDTO.setDescription(food.getDescription());
        foodDTO.setGram(food.getGram());
        foodDTO.setCalories(food.getCalories());
        foodDTO.setType(food.getType());
        return foodDTO;
    }

    public Food mapFoodDTOToFood(FoodDTO foodDTO) {
        mapFoodCommonProperties(food, foodDTO);
        return food;
    }

    public void mapCustomerCommonProperties(Customer customer, CustomerDTO customerDTO) {
        customer.setId(customerDTO.getId());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setCity(customerDTO.getCity());
        customer.setAddress(customerDTO.getAddress());
        customer.setBirthday(customerDTO.getBirthday());
        customer.setGender(customerDTO.getGender());
    }

    public void mapFoodCommonProperties(Food food, FoodDTO foodDTO) {
        food.setName(foodDTO.getName());
        food.setDescription(foodDTO.getDescription());
        food.setGram(foodDTO.getGram());
        food.setCalories(foodDTO.getCalories());
        food.setType(foodDTO.getType());
    }

    public void mapMealCommonProperties(Meal meal, MealDTO mealDTO) {
        meal.setId(mealDTO.getId());
        meal.setName(mealDTO.getName());
        meal.setDescription(mealDTO.getDescription());
        meal.setDay(mealDTO.getDay());
        meal.setType(mealDTO.getType());
    }


    public Mapper() {
    }
}
