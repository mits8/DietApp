package com.example.plan.contactInfo.controller;

import com.example.plan.contactInfo.service.ContactInfoService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contactInfo")
public class ContactInfoController {

    @Autowired
    private ContactInfoService contactInfoService;



    @GetMapping("/find/all")
    public ResponseEntity<List<Map<String, Object>>> findAllContactInfos() {
        return new ResponseEntity<>(contactInfoService.findAllContactInfo(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> findContactInfoById(Map<String, Object> requestMap, @PathVariable Long id) {
        return contactInfoService.getContactInfoById(requestMap, id);
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<ResponseMessage> addContactInfoByCustomerId(@RequestBody Map<String, Object> requestMap, @PathVariable Long id) {
        return contactInfoService.addContactInfoByCustomerId(requestMap, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteContactInfoById(@PathVariable Long id) {
        return contactInfoService.deleteContactInfo(id);
    }
}
