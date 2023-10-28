package com.example.plan.map;

import com.example.plan.customer.entity.Customer;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerWeeklyPlanDTO;
import com.example.plan.dto.food.FoodDTO;
import com.example.plan.dto.meal.MealDTO;
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

    public WeeklyPlanMealCustomerDTO mapWeeklyPlanToMealDTO(WeeklyPlan weeklyPlan) {
        WeeklyPlanMealCustomerDTO weeklyPlanMealCustomerDTO = new WeeklyPlanMealCustomerDTO();
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

    public CustomerWeeklyPlanDTO customerWeeklyPlanDTO(Customer customer) {
        CustomerWeeklyPlanDTO customerWeeklyPlanDTO = new CustomerWeeklyPlanDTO();
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
        MealDTO mealDTO = new MealDTO();
        mealDTO.setId(meal.getId());
        mealDTO.setName(meal.getName());
        mealDTO.setDescription(meal.getDescription());
        mealDTO.setType(meal.getType());
        return mealDTO;
    }

    public CustomerDTO mapCustomerToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
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
        customer.setId(customerDTO.getId());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setCity(customerDTO.getCity());
        customer.setAddress(customer.getAddress());
        customer.setBirthday(customerDTO.getBirthday());
        customer.setGender(customerDTO.getGender());
        return customer;
    }

    public Customer mapCustomerWeeklyPlanDTOToCustomer(CustomerWeeklyPlanDTO customerWeeklyPlanDTO) {
        Customer customer = new Customer();
        customer.setId(customerWeeklyPlanDTO.getId());
        customer.setFirstName(customerWeeklyPlanDTO.getFirstName());
        customer.setLastName(customerWeeklyPlanDTO.getLastName());
        customer.setEmail(customerWeeklyPlanDTO.getEmail());
        customer.setPhone(customerWeeklyPlanDTO.getPhone());
        customer.setCity(customerWeeklyPlanDTO.getCity());
        customer.setAddress(customer.getAddress());
        customer.setBirthday(customerWeeklyPlanDTO.getBirthday());
        customer.setGender(customerWeeklyPlanDTO.getGender());
        return customer;
    }


    public WeeklyPlanDTO weeklyPlanToWeeklyPlanDTO(WeeklyPlan weeklyPlan) {
        WeeklyPlanDTO weeklyPlanDTO = new WeeklyPlanDTO();
        weeklyPlanDTO.setId(weeklyPlan.getId());
        weeklyPlanDTO.setName(weeklyPlan.getName());
        weeklyPlanDTO.setStartDate(weeklyPlan.getStartDate());
        weeklyPlanDTO.setEndDate(weeklyPlan.getEndDate());
        return weeklyPlanDTO;
    }

    public WeeklyPlan mapWeeklyPlanDTOToWeeklyPlan(WeeklyPlanDTO weeklyPlanDTO) {
        WeeklyPlan weeklyPlan = new WeeklyPlan();
        weeklyPlan.setId(weeklyPlanDTO.getId());
        weeklyPlan.setName(weeklyPlanDTO.getName());
        weeklyPlan.setDuration(weeklyPlanDTO.getDuration());
        weeklyPlan.setStartDate(weeklyPlanDTO.getStartDate());
        weeklyPlan.setEndDate(weeklyPlanDTO.getEndDate());
        return weeklyPlan;
    }

    public FoodDTO mapFoodToFoodDTO(Food food) {
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setName(food.getName());
        foodDTO.setDescription(food.getDescription());
        foodDTO.setGram(food.getGram());
        foodDTO.setCalories(food.getCalories());
        foodDTO.setType(food.getType());
        return foodDTO;
    }

    public Food mapFoodDTOToFood(FoodDTO foodDTO) {
        Food food = new Food();
        food.setName(foodDTO.getName());
        food.setDescription(foodDTO.getDescription());
        food.setGram(foodDTO.getGram());
        food.setCalories(foodDTO.getCalories());
        food.setType(foodDTO.getType());
        return food;
    }



    public Mapper() {
    }
}
