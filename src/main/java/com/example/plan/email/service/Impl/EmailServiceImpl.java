package com.example.plan.email.service.Impl;

import com.example.plan.email.service.EmailService;
import com.example.plan.security.auth.service.Impl.AuthEncryptDecrypt;
import com.example.plan.user.dto.EmailDTO;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username")
    private String fromEmail;

    private String subject = "Προσοχή";

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthEncryptDecrypt authEncryptDecrypt;

    private final JavaMailSender mailSender;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public ResponseEntity<String> sendEmail(EmailDTO emailDTO) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailDTO.getTo());
            //mimeMessageHelper.setCc(emailDTO.getCc());
            mimeMessageHelper.setSubject(emailDTO.getSubject());
            mimeMessageHelper.setText(emailDTO.getBody());

            if (emailDTO.getFile() != null) {
                for (int i = 0; i < emailDTO.getFile().length; i++) {
                    mimeMessageHelper.addAttachment(
                            emailDTO.getFile()[i].getOriginalFilename(),
                            new ByteArrayResource(emailDTO.getFile()[i].getBytes()));
                }
            }
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>("Το email στάλθηκε!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> forgotPassword(EmailDTO emailDTO) throws Exception {
        try {
            String email = emailDTO.getEmail();
            UserInfo user = userInfoRepository.findUserByEmail(email);
            String encryptedPassword = user.getPassword();
            String password = authEncryptDecrypt.decrypt(encryptedPassword);
            String contactInfo = user.getContactInfo();
            String checkContactInfo = emailDTO.getContactInfo();
            if(!Objects.isNull(user) && contactInfo.equals(checkContactInfo)) {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                mimeMessageHelper.setFrom(fromEmail);
                mimeMessageHelper.setTo(emailDTO.getTo());
                //mimeMessageHelper.setCc(emailDTO.getCc());
                mimeMessageHelper.setSubject(subject);
                String htmlMsg = "<html><head><meta charset=\"UTF-8\"></head><body>" +
                        "<p><b>Πρόσβαση στο Σύστημα Διαχείρησης Διατροφής</b><br>\n" +
                        "<b>Όνομα Χρήστη: </b>" + user.getEmail() + "<br>\n" +
                        "<b>Κωδικός: </b>" + password + "<br>\n" +
                        "<a href=\"http://localhost:8081/auth/login-\">Click here to login</a></p>";
                mimeMessageHelper.setText(htmlMsg, true);
                mailSender.send(mimeMessage);
                return new ResponseEntity<>("Το email στάλθηκε!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Λάθος email ή τηλέφωνο..", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        return new ResponseEntity<>("Το email ΔΕΝ στάλθηκε..", HttpStatus.BAD_REQUEST);
    }
}