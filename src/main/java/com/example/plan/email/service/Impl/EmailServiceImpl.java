package com.example.plan.email.service.Impl;

import com.example.plan.dto.email.EmailRequest;
import com.example.plan.email.service.EmailService;
import com.example.plan.security.auth.service.Impl.AuthEncryptDecrypt;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import com.example.plan.utils.ResponseMessage;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${file.dest-dir}")
    private String destDir;

    private String subject = "Προσοχή";

    private String subjectOfDietProgram = "Πρόγραμμα Διατροφής";

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthEncryptDecrypt authEncryptDecrypt;

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public ResponseEntity<ResponseMessage> sendEmail(EmailRequest emailRequest) {
        try {

        //    Customer existingCustomer = customerRepository.findByEmail((String) requestMap.get("email"));
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailRequest.getCustomerEmail());
            //mimeMessageHelper.setCc(emailDTO.getCc());
            mimeMessageHelper.setSubject(subjectOfDietProgram);
            mimeMessageHelper.setText(emailRequest.getBody(), true);

            if (emailRequest.getFileName() != null) {
                Path filePath = Paths.get(destDir, emailRequest.getFileName());
                mimeMessageHelper.addAttachment(emailRequest.getFileName(), new FileDataSource(filePath.toFile()));

                mailSender.send(mimeMessage);
                String message = "Το email στάλθηκε!";
                ResponseMessage response = new ResponseMessage(message, emailRequest.getFileName());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "Το email δεν στάλθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("{}", e);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> forgotPassword(Map<String, Object> requestMap) {
        try {
            String email = (String) requestMap.get("email");
            UserInfo user = userInfoRepository.findUserByEmail(email);
            String encryptedPassword = user.getPassword();
            String password = authEncryptDecrypt.decrypt(encryptedPassword);
            /*String contactInfo = user.getStringContactInfo();*/
            String checkContactInfo = (String) requestMap.get("contactInfo");
            if(!Objects.isNull(user) /*&& contactInfo.equals(checkContactInfo)*/) {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                mimeMessageHelper.setFrom(fromEmail);
                mimeMessageHelper.setTo(email);
                //mimeMessageHelper.setCc(emailDTO.getCc());
                mimeMessageHelper.setSubject(subject);
                String htmlMsg = "<html><head><meta charset=\"UTF-8\"></head><body>" +
                        "<p><b>Πρόσβαση στο Σύστημα Διαχείρησης Διατροφής</b><br>\n" +
                        "<b>Όνομα Χρήστη: </b>" + user.getContactInfo().getEmail() + "<br>\n" +
                        "<b>Κωδικός: </b>" + password + "<br>\n" +
                        "<a href=\"http://localhost:8081/auth/login-\">Click here to login</a></p>";
                mimeMessageHelper.setText(htmlMsg, true);
                mailSender.send(mimeMessage);
                String message = "Ο κωδικός στάλθηκε στο email!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            }
            String message = "Λάθος email ή τηλέφωνο..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}