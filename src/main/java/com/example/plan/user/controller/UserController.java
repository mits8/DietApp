package com.example.plan.user.controller;

import com.example.plan.security.auth.service.Impl.AuthServiceImpl;
import com.example.plan.security.config.filter.JwtService;
import com.example.plan.user.dto.ChangePasswordDTO;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import com.example.plan.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/singUp")
    public ResponseEntity<String> singUp (@RequestBody UserInfo userInfo){
        return (authService.singUp(userInfo));
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserInfo>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<UserInfo>> findById(@PathVariable int id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

   // @GetMapping("/checkToken")

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        return userService.changePassword(changePasswordDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateUser(@RequestBody UserInfo userInfo, @PathVariable int id) {
        return userService.updateUser(userInfo, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        UserInfo userInfo = userInfoRepository.findUserById(id);
        return  new ResponseEntity<>(userService.deleteUser(userInfo, id), HttpStatus.OK);
    }

}