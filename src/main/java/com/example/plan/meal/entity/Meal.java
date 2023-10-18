package com.example.plan.meal.entity;

import com.example.plan.customer.entity.Customer;
import com.example.plan.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "quantity")
    private double quantity;

    @Enumerated(EnumType.STRING)
    private Type type;

    /*@OneToMany(mappedBy = "meal",cascade = CascadeType.ALL)
    Set<Food> foods = new HashSet<>();*/

    /*@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;*/
}
