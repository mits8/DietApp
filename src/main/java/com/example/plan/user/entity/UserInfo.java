package com.example.plan.user.entity;

import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Email
    private String email;
    private String password;
    private String contactInfo;
    private boolean isLoggedIn;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "userInfo", fetch = FetchType.EAGER)
    private List<DietCustomer> customers = new ArrayList<>();

}
