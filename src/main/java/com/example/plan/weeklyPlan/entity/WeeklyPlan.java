package com.example.plan.weeklyPlan.entity;

import com.example.plan.customer.entity.Customer;
import com.example.plan.enums.Duration;
import com.example.plan.meal.entity.Meal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weekly_plan")
@ToString(exclude = "meals")
public class WeeklyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "duration")
    @Enumerated(EnumType.STRING)
    private Duration duration;

    @ManyToMany
    @JoinTable(
            name = "weekly_plan_meal",
            joinColumns = @JoinColumn(name = "weekly_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private List<Meal> meals = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "customer_weekly_plan",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "weekly_plan_id")
    )

    private List<Customer> customers = new ArrayList<>();

}
