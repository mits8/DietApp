package com.example.plan.contactInfo.service.Impl;

import com.example.plan.contactInfo.entity.ContactInfo;
import com.example.plan.contactInfo.repository.ContactInfoRepository;
import com.example.plan.contactInfo.service.ContactInfoService;
import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.enums.Role;
import com.example.plan.utils.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContactInfoServiceImpl implements ContactInfoService {

    @Autowired
    private ContactInfoRepository contactInfoRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Map<String, Object>> findAllContactInfo() {
        return contactInfoRepository.findAll().stream()
                .map(contactInfo -> {
                    String fullName;
                    String role;
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", contactInfo.getContactInfoId());
                    if (contactInfo.getDietCustomer() != null) {
                       fullName = contactInfo.getDietCustomer().getFirstname() + " " + contactInfo.getDietCustomer().getSurname();
                       role = Role.CUSTOMER.toString();
                    }else {
                        fullName = contactInfo.getUserInfo().getUsername();
                        role = contactInfo.getUserInfo().getRole().toString();
                    }
                    map.put("fullName", fullName);
                    map.put("role", role);
                    map.put("mobilePhone", contactInfo.getMobilePhone());
                    map.put("email", contactInfo.getEmail());
                    map.put("phone", contactInfo.getPhone());
                    return map;
                }).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<ResponseMessage> getContactInfoById(Map<String, Object> requestMap, Long id) {
        try {
            Optional<DietCustomer> existingCustomer = customerRepository.findById(id);
            if (existingCustomer.isPresent()) {
                Optional<ContactInfo> existingContact = contactInfoRepository.findByEmail(existingCustomer.get().getContactInfo().getEmail());
                if (existingContact.isPresent()) {
                    requestMap.put("contactInfoId", existingContact.get().getContactInfoId());
                    requestMap.put("mobilePhone", existingContact.get().getMobilePhone());
                    requestMap.put("email", existingContact.get().getEmail());
                    requestMap.put("phone", existingContact.get().getPhone());

                } else {
                    String message = "Ο πελάτης δεν βρέθηκε.";
                    ResponseMessage response = new ResponseMessage(message, null);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            } else {
                String message = "Τα στοιχεία επικοινωνίας δεν βρέθηκαν.";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        String message = "Τα στοιχεία επικοινωνίας!";
        ResponseMessage responseMessage = new ResponseMessage<>(message, requestMap);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> addContactInfoByCustomerId(Map<String, Object> requestMap, Long id) {
        try {
            Optional<DietCustomer> existingCustomer = customerRepository.findById(id);
            if (existingCustomer.isPresent()) {
                ContactInfo customerInfo = new ContactInfo();
                customerInfo.setMobilePhone(requestMap.get("mobilePhone").toString());
                Optional<ContactInfo> existingContactInfo = contactInfoRepository.findByEmail((String) requestMap.get("email"));
                if (!existingContactInfo.isPresent()) {
                    customerInfo.setEmail((String) requestMap.get("email"));
                }else {
                    String message = "Το email υπάρχει ΄ήδη.";
                    ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                customerInfo.setPhone(requestMap.get("phone").toString());
                customerInfo.setDietCustomer(existingCustomer.get());
                customerInfo.setDietCustomer(existingCustomer.get());
                contactInfoRepository.save(customerInfo);
            } else {
                String message = "Ο πελάτης δεν βρέθηκε.";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            log.error(e.getMessage());
        }
        String message = "Τα στοιχεία επικοινωνίας προσθέθηκαν!";
        ResponseMessage responseMessage = new ResponseMessage<>(message, requestMap);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteContactInfo(Long id) {
        try {
            Optional<ContactInfo> existingContactInfo = contactInfoRepository.findById(id);
            if (existingContactInfo.isPresent()) {
                contactInfoRepository.deleteById(id);
                String message = "Η επαφή διαγράφτηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error deleting customer", e);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
