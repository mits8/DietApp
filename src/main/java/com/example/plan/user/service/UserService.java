package com.example.plan.user.service;

import com.example.plan.user.dto.ChangePasswordDTO;
import com.example.plan.user.entity.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserInfo> findAll();

    Optional<UserInfo> findById(int id) throws UsernameNotFoundException;

    ResponseEntity<String> singUp(UserInfo userInfo);

    ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO);

    ResponseEntity<String> updateUser(UserInfo userInfo, int id);

    ResponseEntity<String> deleteUser(int id);
}
