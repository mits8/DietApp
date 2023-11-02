package com.example.plan.utils.meal;


import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealResponseMessage {

    private String message;
    private MealDTO mealDTO;
    private MealFoodDTO mealFoodDTO;

    public MealResponseMessage(String message, MealDTO mealDTO) {
        this.message = message;
        this.mealDTO = mealDTO;
    }

    public MealResponseMessage(String message, MealFoodDTO mealFoodDTO) {
        this.message = message;
        this.mealFoodDTO = mealFoodDTO;
    }
}
