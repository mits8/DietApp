package com.example.plan.user.service.Impl;

import com.example.plan.enums.Role;
import com.example.plan.security.auth.service.Impl.AuthEncryptDecrypt;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import com.example.plan.user.service.UserService;
import com.example.plan.utils.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AuthEncryptDecrypt authEncryptDecrypt;
    @Autowired
    JavaMailSender javaMailSender;
    @Override
    public ResponseEntity<ResponseMessage>  findAll() {
        List<UserInfo> users = userInfoRepository.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (mapList != null) {
            for (UserInfo user : users) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("name", user.getName());
                map.put("email", user.getEmail());
                map.put("contactInfo", user.getContactInfo());
                map.put("isLoggedIn", user.isLoggedIn());
                map.put("role", user.getRole());
                mapList.add(map);
            }
            String message = "Οι χρήστες βρέθηκαν!";
            ResponseMessage response = new ResponseMessage(message, mapList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        String message = "Οι χρήστες δεν βρέθηκε..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> findById(int id) throws UsernameNotFoundException {
        Optional<UserInfo> existingUser = userInfoRepository.findUserInfoById(id);
        Map<String, Object> map = new HashMap<>();
        if (existingUser.isPresent()) {
            UserInfo user = existingUser.get();
            map.put("id", user.getId());
            map.put("name", user.getName());
            map.put("email", user.getEmail());
            map.put("password", user.getPassword());
            map.put("contactInfo", user.getContactInfo());
            map.put("isLoggedIn", user.isLoggedIn());
            map.put("role", user.getRole());

            String message = "Ο χρήστης " + user.getName() + " βρέθηκε!";
            ResponseMessage response = new ResponseMessage(message, map);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        String message = "Ο χρήστης δεν βρέθηκε..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> singUp(Map<String, Object> requestMap) {
        try {
            String userEmail = (String) requestMap.get("email");
            UserInfo existingUser = userInfoRepository.findUserByEmail(userEmail);
            if (Objects.isNull(existingUser)) {
                String name = (String) requestMap.get("name");
                String password = (String) requestMap.get("password");
                String contactInfo = (String) requestMap.get("contactInfo");
                Role role = Role.valueOf((String) requestMap.get("role"));

                UserInfo newUser = new UserInfo();
                newUser.setName(name);
                newUser.setEmail(userEmail);
                newUser.setPassword(authEncryptDecrypt.encrypt(password));
                newUser.setContactInfo(contactInfo);
                newUser.setRole(role);
                //newUser.setLoggedIn(true);

                userInfoRepository.save(newUser);

                String message = "Ο χρήστης " + userEmail + " γράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            String message = "Το email " + userEmail + "' υπάρχει ήδη.";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος.";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> changePassword(Map<String, Object> requestMap) {
        try {
            String email = (String) requestMap.get("email");
            UserInfo userInfo = userInfoRepository.findUserByEmail(email);

            if (!userInfo.equals(null) || userInfo != null) {

                String passwordFromDatabase = userInfo.getPassword();
                String declaredOldPassword = (String) requestMap.get("oldPassword");
                String newPassword = (String) requestMap.get("newPassword");

                boolean checkPassword = authEncryptDecrypt.checkPassword(declaredOldPassword, passwordFromDatabase);

                if (checkPassword) {
                    userInfo.setPassword(authEncryptDecrypt.encrypt(newPassword));
                    userInfoRepository.save(userInfo);

                    String message = "Ο κωδικός σας άλλαξε επιτυχώς!";
                    ResponseMessage response = new ResponseMessage(message, null);
                    return new ResponseEntity<>(response , HttpStatus.OK);
                }
                String message = "Ο παλιός κωδικός είναι λάθος..";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);    }

    @Override
    public ResponseEntity<ResponseMessage> updateUser(Map<String, Object> requestMap, int id) {
        try {
            Optional<UserInfo> existingUser = userInfoRepository.findById(id);
            if (existingUser.isPresent()) {
                UserInfo updateUser = existingUser.get();
                updateUser.setName((String) requestMap.get("name"));
                updateUser.setEmail((String) requestMap.get("email"));
                updateUser.setContactInfo((String) requestMap.get("contactInfo"));
                updateUser.setRole(Role.valueOf((String) requestMap.get("role")));
                updateUser.setLoggedIn((Boolean) requestMap.get("isLoggedIn"));
                userInfoRepository.save(updateUser);
                String message = "Ο χρήστης ενημερώθηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "Ο χρήστης ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, requestMap);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteUser(int id) {
        try {
            Optional<UserInfo> user = userInfoRepository.findById(id);
            if (user.isPresent()) {
                userInfoRepository.deleteById(id);
                String message = "Ο χρήστης διαγράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                String message = "Ο χρήστης ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);    }
}
