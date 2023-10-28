package com.example.plan.dto.weeklyPlan;

import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.customer.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyPlanMealCustomerDTO {

    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<MealDTO> meals = new ArrayList<>();
    private List<CustomerDTO> customers = new ArrayList<>();

}
