package com.example.plan.security.auth.service.Impl;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.example.plan.PlanUtils;
import com.example.plan.security.auth.AuthRequest;
import com.example.plan.security.auth.LogoutRequest;
import com.example.plan.security.auth.service.AuthService;
import com.example.plan.security.config.filter.JwtService;
import com.example.plan.constants.PlanConstants;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
    public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    /*public String singUp(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return "user added to system ";
    }
*/

    @Override
    public ResponseEntity<String> singUp(UserInfo userInfo) {

        try {
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            userInfoRepository.save(userInfo);
            if (userInfo.isLoggedIn()) {
                userInfo.setLoggedIn(true);
            }
        }catch (Exception ex) {
            log.info("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Ο χρήστης " + userInfo.getName() + " γράφτηκε επιτυχώς!" + "\"}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> login(AuthRequest authRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                UserInfo user = userInfoRepository.findByName(authRequest.getUsername()).orElseThrow();
                user.setLoggedIn(true);
                return new ResponseEntity<>("{\"message\":\"" + "Καλωςήρθατε!", HttpStatus.OK);
            } else {
                return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Λάθος Διαπιστευτήρια" + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> userLogout(LogoutRequest logoutRequest) {
        try {
            UserInfo user = userInfoRepository.findByName(logoutRequest.getUsername()).orElseThrow();
            if (user != null) {
                user.setLoggedIn(false);
                userInfoRepository.save(user);
                return new ResponseEntity<>("{\"message\":\"" + "Αποσυνδεθήκατε επιτυχώς!", HttpStatus.OK);
            } else {
                return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Λάθος Διαπιστευτήρια" + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> auth(AuthRequest authRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return new ResponseEntity<>("token: " + jwtService.generateToken(authRequest.getUsername()), HttpStatus.OK);
            } else {
                return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Λάθος Διαπιστευτήρια" + "\"}", HttpStatus.BAD_REQUEST);
    }
}
