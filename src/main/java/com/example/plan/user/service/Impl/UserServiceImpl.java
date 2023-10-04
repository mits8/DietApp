package com.example.plan.user.service.Impl;

import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import com.example.plan.security.config.filter.JwtService;
import com.example.plan.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public List<UserInfo> usersList = null;

    @Override
    public List<UserInfo> findAll() {
        List<UserInfo> findAll = userInfoRepository.findAll();
        return findAll;
    }



    @Override
    public Optional<UserInfo> findById(int id) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userInfoRepository.findUserInfoById(id);
        return userInfo;
    }
}
