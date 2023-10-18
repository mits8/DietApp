package com.example.plan.weeklyPlan.entity;

import com.example.plan.enums.Day;
import com.example.plan.enums.Type;
import com.example.plan.meal.entity.Meal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private Day day;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToMany
    @JoinTable(name = "weekly_plan_meal",
            joinColumns = @JoinColumn(name = "weeklyPlan_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id"))

    private Set<Meal> meals = new HashSet<>();

}
