package com.example.plan.contactInfo.entity;

import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.user.entity.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact_info", schema = "plan")
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phone;
    private String email;
    private String mobilePhone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id")
    @ToString.Exclude
    private UserInfo userInfo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_customer_id")
    @ToString.Exclude
    private DietCustomer dietCustomer;

}
