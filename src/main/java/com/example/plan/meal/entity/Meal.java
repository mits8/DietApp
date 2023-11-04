package com.example.plan.meal.entity;

import com.example.plan.enums.Day;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.plan.entity.Plan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meal")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "quantity")
    private String quantity;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private Day day;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "meal_food",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foods = new ArrayList<>();

    @ManyToMany(mappedBy = "meals")
    private List<Plan> plans = new ArrayList<>();

}
