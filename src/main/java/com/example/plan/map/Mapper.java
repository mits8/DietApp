package com.example.plan.map;

import com.example.plan.dto.*;
import com.example.plan.customer.entity.Customer;
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
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());

        return customerDTO;
    }

    public WeeklyPlanDTO weeklyPlanToWeeklyPlanDTO(WeeklyPlan weeklyPlan) {
        WeeklyPlanDTO weeklyPlanDTO = new WeeklyPlanDTO();
        weeklyPlanDTO.setId(weeklyPlanDTO.getId());
        weeklyPlan.setName(weeklyPlan.getName());
        weeklyPlanDTO.setStartDate(weeklyPlan.getStartDate());
        weeklyPlanDTO.setEndDate(weeklyPlan.getEndDate());

        return weeklyPlanDTO;
    }

    public Mapper() {
    }
}
