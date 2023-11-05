package com.example.plan.dto.Plan;

import com.example.plan.dto.food.FoodDTO;
import com.example.plan.dto.meal.MealDTO;
import com.example.plan.enums.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDTO {

    private int id;
    private String name;
    private Duration duration;
    private LocalDate startDate;
    private LocalDate endDate;

    List<MealDTO> mealDTOS = new ArrayList<>();
    List<FoodDTO> foodDTOS = new ArrayList<>();
}
