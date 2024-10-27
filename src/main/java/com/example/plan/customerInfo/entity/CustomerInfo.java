package com.example.plan.customerInfo.entity;

import com.example.plan.customer.entity.DietCustomer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Entity
@Data
@ToString(exclude = "meals")
@Table(name = "customerInfo")
public class CustomerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "createdDate")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "age")
    private Integer age;

    @Column(name = "height")
    private Double height;

    @Column(name = "water")
    private Double water;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "muscleMass")
    private Double muscleMass;

    @Column(name = "bodyFatMass")
    private Double bodyFatMass;

    @Column(name = "fat")
    private Double fat;

    @NumberFormat(pattern = "#,##0.0")
    @Column(name = "bmr")
    private Double bmr;

    @NumberFormat(pattern = "#,##0.0")
    @Column(name = "tdee")
    private Double tdee;

    @Column(name = "activityLevel")
    private Integer activityLevel;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private DietCustomer customer;


}
