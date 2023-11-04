package com.example.plan.dto.plan;

import com.example.plan.enums.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDTO {

    private int id;
    private String name;
    private Duration duration;
    private LocalDate startDate;
    private LocalDate endDate;
}
