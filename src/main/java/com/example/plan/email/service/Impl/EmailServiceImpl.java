package com.example.plan.email.service.Impl;

import com.example.plan.email.service.EmailService;
import com.example.plan.security.auth.AuthRequest;
import com.example.plan.user.dto.EmailDTO;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import jakarta.mail.MessagingException;
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

    @Autowired
    private UserInfoRepository userInfoRepository;

    private final JavaMailSender mailSender;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public ResponseEntity<String> sendEmail(EmailDTO emailDTO) {

        /*SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("xatzic8@gmail.com");
        simpleMailMessage.setTo(emailDTO.getTo());
        simpleMailMessage.setCc(emailDTO.getCc());
        simpleMailMessage.setSubject(emailDTO.getSubject());
        simpleMailMessage.setText(emailDTO.getBody());

        this.mailSender.send(simpleMailMessage);*/


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
        return new ResponseEntity<>("{\"message\":\"" + "Το email στάλθηκε!" + "\"}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> forgotPassword(EmailDTO emailDTO) {
        try {
            UserInfo userInfo = userInfoRepository.findUserByEmail(emailDTO.getEmail());
            if(!Objects.isNull(userInfo)) {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                mimeMessageHelper.setFrom(fromEmail);
                mimeMessageHelper.setTo(emailDTO.getTo());
                //mimeMessageHelper.setCc(emailDTO.getCc());
                mimeMessageHelper.setSubject(emailDTO.getSubject());
                String htmlMsg = "<html><head><meta charset=\"UTF-8\"></head><body>" +
                        "<p><b>Πρόσβαση στο Σύστημα Διαχείρησης Διατροφής</b><br>\n" +
                        "<b>Όνομα Χρήστη: </b>" + emailDTO.getTo() + "<br>\n" +
                        "<b>Κωδικός: </b>" + userInfo.getPassword() + "<br>\n" +
                        "<a href=\"http://localhost:8081/email/forgotMail\">Click here to login</a></p>";
                mimeMessageHelper.setText(htmlMsg, true);
                mailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Το email στάλθηκε!" + "\"}", HttpStatus.OK);
    }
}