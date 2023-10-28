package com.example.plan.utils.food;

import com.example.plan.dto.food.FoodDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodResponseMessage {

    private String message;
    private FoodDTO foodDTO;

    public FoodResponseMessage(String message, FoodDTO foodDTO) {
        this.message = message;
        this.foodDTO = foodDTO;
    }
}
