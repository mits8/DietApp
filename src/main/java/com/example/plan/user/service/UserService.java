package com.example.plan.user.service;

import com.example.plan.user.entity.UserInfo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserInfo> findAll();

    Optional<UserInfo> findById(int id) throws UsernameNotFoundException;
}
