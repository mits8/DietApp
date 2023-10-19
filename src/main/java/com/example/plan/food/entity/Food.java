package com.example.plan.food.entity;

import com.example.plan.customer.entity.Customer;
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

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "gram")
    private double gram;

    @Column(name = "calories")
    private double calories;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToMany(mappedBy = "foods")
    private Set<Meal> meals = new HashSet<>();
}
