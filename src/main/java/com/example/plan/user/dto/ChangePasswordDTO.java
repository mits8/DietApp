package com.example.plan.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {

    private String email;
    private String oldPassword;
    private String newPassword;

}
