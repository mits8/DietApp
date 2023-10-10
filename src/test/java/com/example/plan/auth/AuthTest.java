package com.example.plan.auth;

import com.example.plan.security.auth.service.Impl.AuthEncryptDecrypt;
import com.example.plan.security.auth.service.Impl.AuthServiceImpl;
import com.example.plan.user.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthTest {

    @Mock
    AuthServiceImpl authService;
    @Mock
    UserInfoRepository userInfoRepository;
    @Mock
    AuthEncryptDecrypt authEncryptDecrypt;
    @Test
    void encryptionTest() throws Exception {
        String text = "123";
        authEncryptDecrypt = new AuthEncryptDecrypt();
        String encryptedText = authEncryptDecrypt.encrypt(text);
        System.out.println("Κρυπτογραφημένο:" + encryptedText);

        String decrypt = authEncryptDecrypt.decrypt(encryptedText);
        assertEquals("123", decrypt);
        System.out.println("Αποκρυπτογραφημένο:" + decrypt);
    }

    @Test
    void generateToken() throws Exception {
        String text = "123";
    }



}

