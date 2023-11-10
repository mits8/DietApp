package com.example.plan.food.entity;

import com.example.plan.enums.Type;
import com.example.plan.meal.entity.Meal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "meals")
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;


    @Column(name = "gram")
    private double gram;

    @Column(name = "calories")
    private double calories;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToMany(mappedBy = "foods")
    private List<Meal> meals = new ArrayList<>();
}
