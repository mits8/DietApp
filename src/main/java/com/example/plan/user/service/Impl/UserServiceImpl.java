package com.example.plan.user.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.security.auth.service.Impl.AuthEncryptDecrypt;
import com.example.plan.dto.ChangePasswordDTO;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import com.example.plan.user.service.UserService;
import com.example.plan.utils.PlanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public ResponseEntity<String> singUp(UserInfo userInfo) {
        try {
            UserInfo userEmail = userInfoRepository.findUserByEmail(userInfo.getEmail());
            if (Objects.isNull(userEmail)) {
                userInfo.setPassword(authEncryptDecrypt.encrypt(userInfo.getPassword()));
                userInfoRepository.save(userInfo);
                if (userInfo.isLoggedIn()) {
                    userInfo.setLoggedIn(true);
                }
                return new ResponseEntity<>("Ο χρήστης " + userInfo.getEmail() + " γράφτηκε επιτυχώς!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Το email " + userInfo.getEmail() + " υπάρχει ήδη..", HttpStatus.OK);

        }catch (Exception ex) {
            log.info("{}", ex);
        }
        return new ResponseEntity<>("Ο χρήστης " + userInfo.getEmail() + " γράφτηκε επιτυχώς!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO) {
        try {
            String email = changePasswordDTO.getEmail();
            UserInfo userInfo = userInfoRepository.findUserByEmail(email);

            if (!userInfo.equals(null) || userInfo != null) {

                String passwordFromDatabase = userInfo.getPassword();
                String declaredOldPassword = changePasswordDTO.getOldPassword();
                String newPassword = changePasswordDTO.getNewPassword();

                boolean checkPassword = authEncryptDecrypt.checkPassword(declaredOldPassword, passwordFromDatabase);

                if (checkPassword) {
                    userInfo.setPassword(authEncryptDecrypt.encrypt(newPassword));
                    userInfoRepository.save(userInfo);

                    return new ResponseEntity<>("Ο κωδικός σας άλλαξε επιτυχώς!", HttpStatus.OK);
                }
                return new ResponseEntity<>("Ο παλιός κωδικός είναι λάθος!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>("Το email " + changePasswordDTO.getEmail() + " είναι λάθος..", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> updateUser(UserInfo userInfo, int id) {
        try {
            Optional<UserInfo> existingUser = userInfoRepository.findById(id);
            if (existingUser.isPresent()) {
                UserInfo updateUser = existingUser.get();
                updateUser.setName(userInfo.getName());
                updateUser.setEmail(userInfo.getEmail());
                updateUser.setContactInfo(userInfo.getContactInfo());
                updateUser.setRole(userInfo.getRole());
                userInfoRepository.save(updateUser);
            } else {
                return new ResponseEntity<>( "Ο χρήστης ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Ο χρήστης ενημερώθηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteUser(int id) {
        try {
            Optional<UserInfo> user = userInfoRepository.findById(id);
            if (user.isPresent()) {
                userInfoRepository.deleteById(id);
            } else {
                return new ResponseEntity<>("Ο χρήστης ΔΕΝ βρέθηκε..", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<> ("Ο χρήστης διαγράφτηκε επιτυχώς!", HttpStatus.OK);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
