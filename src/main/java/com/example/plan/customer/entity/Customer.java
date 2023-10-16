package com.example.plan.customer.entity;

import com.example.plan.enums.Gender;
import com.example.plan.food.entity.Food;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Length(min= 5, max = 30)
    @Column(name = "firstName")
    private String firstName;

    @Length(min= 5, max = 30)
    @Column(name = "lastName")
    private String lastName;

    @NotNull
    @Email
    @Column(name = "email",  unique = true)
    private String email;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /*@OneToMany(mappedBy = "customer")
    private List<Food> foods = new ArrayList<>();*/

    public Customer(Customer customer) {
    }
}
