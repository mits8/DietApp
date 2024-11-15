package com.example.plan.customer.entity;

import com.example.plan.contactInfo.entity.ContactInfo;
import com.example.plan.customerInfo.entity.CustomerInfo;
import com.example.plan.enums.Gender;
import com.example.plan.plan.entity.Plan;
import com.example.plan.user.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"contactInfo", "plans", "customerInfos", "userInfo"})
@Table(name = "diet_customer", schema = "plan")
public class DietCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname")
    private String firstname;
    
    @Column(name = "surname")
    private String surname;

    @Column(name = "password")
    private String password;

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

    @ManyToMany (mappedBy = "customers")
    private List<Plan> plans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserInfo userInfo;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,  orphanRemoval = true)
    @JsonIgnore
    private List<CustomerInfo> customerInfos = new ArrayList<>();

    @OneToOne(mappedBy = "dietCustomer", cascade = CascadeType.ALL)
    private ContactInfo contactInfo;


}
