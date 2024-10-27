package com.example.plan.dto.customer;

import com.example.plan.dto.Plan.PlanDTO;
import com.example.plan.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPlanDTO extends CustomerDTO {

    private Long id;
    private String firstname;

    private String surname;

    private String email;

    @Length(min = 10, max = 10)
    private String phone;

    private String city;

    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private List<PlanDTO> planDTOS = new ArrayList<>();
}
