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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    public ResponseEntity<String> singUp(UserInfo userInfo) {

        try {
            UserInfo userEmail = userInfoRepository.findUserByEmail(userInfo.getEmail());
            if (Objects.isNull(userEmail)) {
                userInfo.setPassword(authEncryptDecrypt.encrypt(userInfo.getPassword()));
                userInfoRepository.save(userInfo);
                if (userInfo.isLoggedIn()) {
                    userInfo.setLoggedIn(true);
                }
                return new ResponseEntity<>("{\"message\":\"" + "Ο χρήστης " + userInfo.getName() + " γράφτηκε επιτυχώς!" + "\"}", HttpStatus.OK);
}
            return new ResponseEntity<>("{\"message\":\"" + "Το email " + userInfo.getEmail() + " υπάρχει ήδη!" + "\"}", HttpStatus.OK);

        }catch (Exception ex) {
            log.info("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Ο χρήστης " + userInfo.getName() + " γράφτηκε επιτυχώς!" + "\"}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> login(AuthRequest authRequest) {
        try{
                String email = authRequest.getEmail();
                UserInfo userInfo = userInfoRepository.findUserByEmail(email);

                boolean userNotNull = userInfo != null && !userInfo.equals(null);
                boolean checkPassword = authEncryptDecrypt.checkPassword(authRequest.getPassword(), userInfo.getPassword());
                userInfo.setLoggedIn(true);

            if ((userNotNull)){
                if (!checkPassword) {
                    return PlanUtils.getResponseEntity(PlanConstants.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("{\"message\":\"" + "Καλωςήρθατε!", HttpStatus.OK);

            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Το email: " + authRequest.getEmail() + " είναι λάθος.." + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> userLogout(LogoutRequest logoutRequest) {
        try {
            String email = logoutRequest.getUsername();
            UserInfo user = userInfoRepository.findUserByEmail(email);
            if (user != null) {
                user.setLoggedIn(false);
                userInfoRepository.save(user);
                return new ResponseEntity<>("{\"message\":\"" + "Αποσυνδεθήκατε επιτυχώς!", HttpStatus.OK);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Λάθος Διαπιστευτήρια" + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> auth(AuthRequest authRequest) {
        try{
           // Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
           // if (authentication.isAuthenticated()) {

            String email = authRequest.getEmail();
            UserInfo userInfo = userInfoRepository.findUserByEmail(email);

             boolean userNotNull = userInfo != null && !userInfo.equals(null);
             boolean checkPassword = authEncryptDecrypt.checkPassword(authRequest.getPassword(), userInfo.getPassword());

            if ((userNotNull)){
                if (!checkPassword) {
                    return PlanUtils.getResponseEntity(PlanConstants.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>("token: " + jwtService.generateToken(authRequest.getEmail()), HttpStatus.OK);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Το email: " + authRequest.getEmail() + " είναι λάθος.." + "\"}", HttpStatus.BAD_REQUEST);
    }
}
