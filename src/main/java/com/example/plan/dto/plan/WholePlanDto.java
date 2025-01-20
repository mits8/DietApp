package com.example.plan.dto.plan;

import com.example.plan.enums.Day;
import com.example.plan.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WholePlanDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String mealName;
    private Day day;
    private Type type;
    private Double calories;
    private LocalDate mealStartDate;
    private LocalDate mealEndDate;
    private String customerFullName;
    private String customerEmail;
    private String fileName;

}