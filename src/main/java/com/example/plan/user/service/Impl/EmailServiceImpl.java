package com.example.plan.user.service.Impl;

import com.example.plan.user.dto.EmailDTO;
import com.example.plan.user.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;
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
}