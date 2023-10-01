package com.example.plan.service.userService.Impl;

import com.example.plan.entity.UserInfo;
import com.example.plan.repository.UserInfoRepository;
import com.example.plan.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserInfo> findUsers() {
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        return userInfoList;
    }

    @Override
    public String singUp(UserInfo userInfo) {
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            userInfoRepository.save(userInfo);
            return "user added to system ";
    }

    @Override
    public List<UserInfo> getUserInfoList() {
        List<UserInfo> userInfo = userInfoRepository.findAll();
        return userInfo;
    }

    List<UserInfo> userInfoList = null;
    @Override
    public UserInfo getUser(int id) {
        return userInfoList.stream()
                .filter(userInfo -> userInfo.getId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("product " + id + " not found"));
    }


}
