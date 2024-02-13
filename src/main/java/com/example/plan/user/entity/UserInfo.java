package com.example.plan.user.entity;

import com.example.plan.customer.entity.Customer;
import com.example.plan.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Email
    private String email;
    private String password;
    private String contactInfo;
    private boolean isLoggedIn;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "userInfo")
    private List<Customer> customers = new ArrayList<>();

}
