package com.example.plan.customerInfo.controller;

import com.example.plan.customer.service.CustomerService;
import com.example.plan.customerInfo.service.CustomerInfoService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customerInformation")
public class CustomerInfoController {

    @Autowired
    private CustomerInfoService customerInfoService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Map<String, Object>>> findCustomerInfo() {
        return new ResponseEntity<>(customerInfoService.findCustomerInfo(), HttpStatus.OK);
    }

    @GetMapping("/customerInfoId{id}")
    public ResponseEntity<Map<String, Object> > findById(@PathVariable Long id) {
        return new ResponseEntity<>(customerInfoService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/add/customerId{id}")
    public ResponseEntity<ResponseMessage> addCustomerInfo(@RequestBody Map<String, Object> requestMap, @PathVariable Long id) {
        return customerInfoService.addCustomerInfo(requestMap, id);
    }

    @PostMapping("/update/customerInfoId{id}")
    public ResponseEntity<ResponseMessage> updateCustomerInfo(@RequestBody Map<String, Object> requestMap, @PathVariable Long id) {
        return customerInfoService.updateCustomerInfo(requestMap, id);
    }

    @GetMapping("/bmi/tdee/customerInfoId{id}")
    public ResponseEntity<ResponseMessage> calculateBMR(@PathVariable Long id) {
        return customerInfoService.calculateBMR(id);
    }

    @DeleteMapping("/delete/customerInfoId{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCustomerInfo(@PathVariable Long  id) {
        return customerInfoService.deleteCustomerInfo(id);
    }

    @DeleteMapping("/delete/all/customerInfo/byCustomerId{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteAllCustomerInfoByCustomerId(@PathVariable Long  id) {
        return customerInfoService.deleteAllCustomerInfoByCustomerId(id);
    }

}
