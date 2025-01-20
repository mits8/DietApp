package com.example.plan.dto.plan;

import com.example.plan.dto.meal.MealDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanMealDTO {

    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<MealDTO> meals = new ArrayList<>();
}
