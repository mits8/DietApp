package com.example.plan.food.entity;

import com.example.plan.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "gram")
    private double gram;

    @Column(name = "calories")
    private double calories;

    /*@ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;*/


}
