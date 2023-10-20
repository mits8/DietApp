package com.example.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyPlanCustomerDTO {

    private int id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CustomerDTO> customers = new ArrayList<>();
}
