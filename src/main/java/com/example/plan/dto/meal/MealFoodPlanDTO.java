package com.example.plan.dto.meal;

import com.example.plan.dto.food.FoodDTO;
import com.example.plan.dto.plan.PlanDTO;
import com.example.plan.enums.Day;
import com.example.plan.enums.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealFoodPlanDTO extends MealFoodDTO{

        private int id;
        private String name;
        private String description;
        private String quantity;
        @Enumerated(EnumType.STRING)
        private Day day;
        @Enumerated(EnumType.STRING)
        private Type type;

        List<FoodDTO> foodDTOS = new ArrayList<>();

        List<PlanDTO> PlanDTOS = new ArrayList<>();

}
