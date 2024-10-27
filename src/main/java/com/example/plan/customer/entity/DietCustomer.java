package com.example.plan.customer.entity;

import com.example.plan.customerInfo.entity.CustomerInfo;
import com.example.plan.enums.Gender;
import com.example.plan.plan.entity.Plan;
import com.example.plan.user.entity.UserInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
//@ToString(exclude = "plans")
@Table(name = "diet_customer")
public class DietCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Length(min= 5, max = 30)
    @Column(name = "firstname")
    private String firstname;

    @Length(min= 5, max = 30)
    @Column(name = "surname")
    private String surname;
    
    @Email
    @Column(name = "email",  unique = true)
    private String email;

    @Column(name = "password")
    private String password;

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

    @ManyToMany (mappedBy = "customers")
    private List<Plan> plans = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<CustomerInfo> customerInfos = new ArrayList<>();


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                '}';
    }

}
