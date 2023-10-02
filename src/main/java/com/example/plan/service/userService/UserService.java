package com.example.plan.service.userService;

import com.example.plan.dto.AuthRequest;
import com.example.plan.entity.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserInfo> findUsers();

    ResponseEntity<String> singUp(UserInfo userInfo);

    ResponseEntity<String> auth(AuthRequest authRequest);

    List<UserInfo> getUserInfoList();

    Optional<UserInfo> getUserById(int id) throws UsernameNotFoundException;
}
