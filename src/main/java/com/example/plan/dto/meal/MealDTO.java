package com.example.plan.dto.meal;

import com.example.plan.enums.Day;
import com.example.plan.enums.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealDTO {

    private int id;
    private String name;
    private String description;
    private String quantity;

    @Enumerated(EnumType.STRING)
    private Day day;
    @Enumerated(EnumType.STRING)
    private Type type;

}
