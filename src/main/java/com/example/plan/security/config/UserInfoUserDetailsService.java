package com.example.plan.security.config;

import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Optional<UserInfo> userInfo = repository.findByEmail(email);
            return userInfo.map(UserInfoUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException("Ο χρήστης " + email + " δεν βρέθηκε"));
        } catch (UsernameNotFoundException ex) {
            log.error("An error occurred while loading user by username: {}", ex);
            throw new UsernameNotFoundException("Σφάλμα κατά τη φόρτωση του χρήστη με email: " + email);
        }
    }
}
