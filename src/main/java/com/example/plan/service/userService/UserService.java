package com.example.plan.service.userService;

import com.example.plan.entity.UserInfo;

import java.util.List;

public interface UserService {

    List<UserInfo> findUsers();

    String singUp(UserInfo userInfo);

    List<UserInfo> getUserInfoList();

    UserInfo getUser(int id);
}
