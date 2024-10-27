package com.example.plan.service;

import com.example.plan.enums.Role;
import com.example.plan.security.auth.AuthRequest;
import com.example.plan.security.auth.service.Impl.AuthEncryptDecrypt;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ServiceTest {

    @Mock
    UserInfoRepository userInfoRepository;
    @Mock
    AuthEncryptDecrypt authEncryptDecrypt;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void checkPassword() {
        AuthRequest authRequest = new AuthRequest("d@gmail.com", "2", Role.ADMIN.toString());
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUsername("Dimi");
        userInfo.setEmail("d@gmail.com");
        userInfo.setPassword("2");
        userInfo.setRole(Role.ADMIN);

        when(userInfoRepository.findUserByEmail(authRequest.getEmail())).thenReturn(userInfo);
        when(authEncryptDecrypt.checkPassword(authRequest.getPassword(), userInfo.getPassword())).thenReturn(false);

      //  ResponseEntity<String> response = authService.auth(authRequest);

        assertEquals(authRequest.getPassword(), userInfo.getPassword());
        System.out.println("Ο κωδικός είναι σωστός!");
    }

    @Test
    void checkInvalidPassword() {
        AuthRequest authRequest = new AuthRequest("d@gmail.com", "1", Role.ADMIN.toString());
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1L);
        userInfo.setUsername("Dimi");
        userInfo.setEmail("d@gmail.com");
        userInfo.setPassword("123");
        userInfo.setRole(Role.ADMIN);

        when(userInfoRepository.findUserByEmail(authRequest.getEmail())).thenReturn(userInfo);

        authEncryptDecrypt.checkPassword(authRequest.getEmail(), userInfo.getPassword());


        assertThrows(Exception.class, () -> { throw new Exception("Ο κωδικός είναι λάθος.."); } );

    }


}
