package com.example.plan.weeklyPlan.dto;

import com.example.plan.enums.Day;
import com.example.plan.enums.Type;
import com.example.plan.meal.dto.MealDTO;
import com.example.plan.meal.entity.Meal;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyPlanMealDTO {

    private int id;
    private String weeklyPlanName;
    @Enumerated(EnumType.STRING)
    private Day day;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Set<MealDTO> meals = new HashSet<>();

    public WeeklyPlanMealDTO(Meal meal, String mealName) {
    }
}
