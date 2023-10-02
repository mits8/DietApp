package com.example.plan.service.userService.Impl;

import com.example.plan.PlanUtils;
import com.example.plan.constants.PlanConstants;
import com.example.plan.dto.AuthRequest;
import com.example.plan.entity.UserInfo;
import com.example.plan.repository.UserInfoRepository;
import com.example.plan.service.JwtService;
import com.example.plan.service.userService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Override
    public List<UserInfo> findUsers() {
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        return userInfoList;
    }

    @Override
    public ResponseEntity<String> singUp(UserInfo userInfo) {
        try {
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            userInfoRepository.save(userInfo);
        }catch (Exception ex) {
            log.info("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Ο χρήστης " + userInfo.getName() + " εγγράφτηκε επιτυχώς!" + "\"}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> auth(AuthRequest authRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
                if (authentication.isAuthenticated()) {
                    return new ResponseEntity<>("{\"token\":\"" + jwtService.generateToken(authRequest.getUsername()), HttpStatus.OK);
                } else {
                    return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
                }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Λάθος Διαπιστευτήρια" + "\"}", HttpStatus.BAD_REQUEST);
    }


    List<UserInfo> userInfoList = null;
    @Override
    public List<UserInfo> getUserInfoList() {
        List<UserInfo> getAllUser = userInfoRepository.findAll();
        return getAllUser;
    }

    @Override
    public Optional<UserInfo> getUserById(int id) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userInfoRepository.findUserInfoById(id);
        return userInfo;
    }
}
