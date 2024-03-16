package com.example.plan.user.controller;

import com.example.plan.user.service.UserService;
import com.example.plan.utils.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/findAll")
    public ResponseEntity<ResponseMessage> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping("/singUp")
    public ResponseEntity<ResponseMessage> singUp (@RequestBody Map<String, Object> requestMap){
        return userService.singUp(requestMap);
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody Map<String, Object> requestMap) {
        return userService.changePassword(requestMap);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> updateUser(@RequestBody Map<String, Object> requestMap,
                                                      @PathVariable int id) {
        return userService.updateUser(requestMap, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }
}