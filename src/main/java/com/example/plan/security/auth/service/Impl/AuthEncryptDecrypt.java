package com.example.plan.security.auth.service.Impl;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Service
public class AuthEncryptDecrypt {

    private static final String AES_SECRET_KEY = "a*88cd3m1kl5&w$3u2&r!5t@8p}0z/3+";
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_CIPHER = "AES/ECB/PKCS5Padding";


    public String encrypt(String plaintext) throws Exception {
        SecretKey secretKey = new SecretKeySpec(AES_SECRET_KEY.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText) throws Exception {
        SecretKey secretKey = new SecretKeySpec(AES_SECRET_KEY.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

    public boolean checkPassword(String inputPassword, String storedEncryptedPassword) {
        try {
            String decryptedPassword = decrypt(storedEncryptedPassword);
            if (inputPassword.equals(decryptedPassword)) {
                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
