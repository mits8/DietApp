package com.example.plan.security.auth.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.security.auth.AuthRequest;
import com.example.plan.security.auth.LogoutRequest;
import com.example.plan.security.auth.service.AuthService;
import com.example.plan.security.config.filter.JwtService;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import com.example.plan.utils.PlanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
    public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthEncryptDecrypt authEncryptDecrypt;

    @Override
    public ResponseEntity<String> generateToken(AuthRequest authRequest) {
        return new ResponseEntity<>("token: " + jwtService.generateToken(authRequest.getEmail()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> login(AuthRequest authRequest) {
        try{
                String email = authRequest.getEmail();
                UserInfo user = userInfoRepository.findUserByEmail(email);

                boolean userNotNull = user != null && !user.equals(null);
                boolean checkPassword = authEncryptDecrypt.checkPassword(authRequest.getPassword(), user.getPassword());
                user.setLoggedIn(true);
                userInfoRepository.save(user);
            if ((userNotNull)){
                if (!checkPassword) {
                    return PlanUtils.getResponseEntity(PlanConstants.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("Καλωςήρθατε!", HttpStatus.OK);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("Το email " + authRequest.getEmail() + " είναι λάθος..", HttpStatus.BAD_REQUEST);
    }



    @Override
    public ResponseEntity<String> auth(AuthRequest authRequest) {
        try{
            String email = authRequest.getEmail();
            UserInfo user = userInfoRepository.findUserByEmail(email);
             boolean userNotNull = user != null && !user.equals(null);
             boolean checkPassword = authEncryptDecrypt.checkPassword(authRequest.getPassword(), user.getPassword());
             user.setLoggedIn(true);
            userInfoRepository.save(user);
            if ((userNotNull)){
                if (!checkPassword) {
                    return PlanUtils.getResponseEntity(PlanConstants.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("token: " + jwtService.generateToken(authRequest.getEmail()), HttpStatus.OK);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("Το email " + authRequest.getEmail() + " είναι λάθος..", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> logout(LogoutRequest logoutRequest) {
        try {
            String email = logoutRequest.getUsername();
            UserInfo user = userInfoRepository.findUserByEmail(email);
            if (user != null) {
                user.setLoggedIn(false);
                userInfoRepository.save(user);
                return new ResponseEntity<>("Αποσυνδεθήκατε επιτυχώς!", HttpStatus.OK);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("Το email " + logoutRequest.getUsername() + " είναι λάθος..", HttpStatus.BAD_REQUEST);
    }
}
