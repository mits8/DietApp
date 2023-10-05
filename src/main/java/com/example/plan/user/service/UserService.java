package com.example.plan.user.service;

import com.example.plan.user.dto.ChangePasswordDTO;
import com.example.plan.user.entity.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    List<UserInfo> findAll();

    Optional<UserInfo> findById(int id) throws UsernameNotFoundException;

    ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO);

    ResponseEntity<String> updateUser(UserInfo userInfo, int id);

    String deleteUser(UserInfo userInfo, int id);
}
