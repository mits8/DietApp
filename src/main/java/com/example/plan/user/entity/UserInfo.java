package com.example.plan.user.entity;

import com.example.plan.contactInfo.entity.ContactInfo;
import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info", schema = "plan")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean isLoggedIn;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "userInfo", fetch = FetchType.EAGER)
    private List<DietCustomer> customers = new ArrayList<>();

    @OneToOne(mappedBy = "userInfo", cascade = CascadeType.ALL)
    private ContactInfo contactInfo;

}
