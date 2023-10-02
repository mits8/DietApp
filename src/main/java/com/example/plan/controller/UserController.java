package com.example.plan.controller;

import com.example.plan.PlanUtils;
import com.example.plan.constants.PlanConstants;
import com.example.plan.dto.AuthRequest;
import com.example.plan.entity.UserInfo;
import com.example.plan.repository.UserInfoRepository;
import com.example.plan.service.JwtService;
import com.example.plan.service.userService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;


    @GetMapping("/findAll")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserInfo>> findAllUsers() {
        return new ResponseEntity<>(userService.getUserInfoList(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Optional<UserInfo>> getProductById(@PathVariable int id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping("/singUp")
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo) {
        return userService.singUp(userInfo);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        return userService.auth(authRequest);
    }
    @PostMapping("/login")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> login (@RequestBody AuthRequest authRequest){
        return userService.auth(authRequest);
    }
}