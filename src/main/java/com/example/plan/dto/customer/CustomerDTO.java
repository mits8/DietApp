package com.example.plan.dto.customer;

import com.example.plan.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private int id;

    private String fullName ;
    private String firstName;

    private String lastName;

    private String email;

    @Length(min = 10, max = 10)
    private String phone;

    private String city;

    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    public CustomerDTO(String errorMessage, Object o) {
    }
}
