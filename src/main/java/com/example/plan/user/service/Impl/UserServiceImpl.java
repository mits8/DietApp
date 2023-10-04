package com.example.plan.user.service.Impl;

import com.example.plan.PlanUtils;
import com.example.plan.constants.PlanConstants;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import com.example.plan.security.config.filter.JwtService;
import com.example.plan.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Override
    public ResponseEntity<String> updateUser(UserInfo userInfo, int id) {
        try {
            Optional<UserInfo> existingUser = userInfoRepository.findById(id);
            if (existingUser.isPresent()) {
                UserInfo updateUser = existingUser.get();
                updateUser.setName(userInfo.getName());
                updateUser.setEmail(userInfo.getEmail());
                updateUser.setPassword(passwordEncoder.encode(userInfo.getPassword()));
                updateUser.setRole(userInfo.getRole());
                userInfoRepository.save(updateUser);
                return new ResponseEntity<>("{\"message\":\"" + "Ο χρήστης " + userInfo.getName() + " ενημερώθηκε επιτυχώς!", HttpStatus.OK);
            } else {
                return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

            }
        } catch (Exception ex) {
            log.info("{}", ex);

        }
        return new ResponseEntity<>("{\"message\":\"" + "Λάθος Διαπιστευτήρια" + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public String deleteUser(UserInfo userInfo , int id) {
        try {
            userInfoRepository.deleteById(id);
                return ("{\"message\":\"" + "Ο χρήστης " + userInfo.getName() + " διαγράφτηκε επιτυχώς!");
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return ("{\"message\":\"" + "Ο χρήστης ΔΕΝ διαγράφτηκε..");
    }
}
