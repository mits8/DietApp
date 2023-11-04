package com.example.plan.dto.food;

import com.example.plan.enums.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDTO {

    private int id;
    private String name;
    private String description;
    private double gram;
    private double calories;
    @Enumerated(EnumType.STRING)
    private Type type;
}
