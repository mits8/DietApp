package com.example.plan.customer.entity;

import com.example.plan.enums.Gender;
import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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


    @ManyToMany
    @JoinTable(
            name = "customer_weekly_plan",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "weekly_plan_id")
    )
    private Set<WeeklyPlan> plans = new HashSet<>();


    public Customer(Customer customer) {
    }
}
