package com.example.plan.security.auth.service.Impl;

import org.springframework.context.annotation.Bean;
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
            encrypt(inputPassword);
            String decryptedPassword = decrypt(storedEncryptedPassword);// Συγκρίνετε τον αποκρυπτογραφημένο αποθηκευμένο κωδικό με τον κωδικό που δώσατε
            return inputPassword.equals(decryptedPassword);
        } catch (Exception ex) {
            // Κατά τη διάρκεια της αποκρυπτογράφησης μπορεί να προκύψει εξαίρεση
            // Μπορείτε να χειριστείτε αυτή την εξαίρεση ανάλογα με τις ανάγκες σας
            ex.printStackTrace();
            return false;
        }
    }

        /*public boolean checkEmail(String inputEmail, String storedEmail) {
            try {
                if (inputEmail.equals(storedEmail)){
                    return true;
                }
                } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

    boolean checkEmail = authEncryptDecrypt.checkEmail(authRequest.getEmail(), userInfo.getEmail());*/
}
