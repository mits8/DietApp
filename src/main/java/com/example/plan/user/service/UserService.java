package com.example.plan.user.service;

import com.example.plan.utils.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseEntity<ResponseMessage>  findAll();

    ResponseEntity<ResponseMessage> findById(int id) throws UsernameNotFoundException;

    ResponseEntity<ResponseMessage> singUp(Map<String, Object> requestMap);

    ResponseEntity<ResponseMessage> changePassword(Map<String, Object> requestMap);

    ResponseEntity<ResponseMessage> updateUser(Map<String, Object> requestMap, int id);

    ResponseEntity<ResponseMessage> deleteUser(int id);
}
