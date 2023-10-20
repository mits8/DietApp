package com.example.plan.dto;

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
