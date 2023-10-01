package com.example.plan.controller;

import com.example.plan.dto.AuthRequest;
import com.example.plan.dto.Product;
import com.example.plan.entity.UserInfo;
import com.example.plan.service.JwtService;
import com.example.plan.service.productService.ProductService;
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

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/new")
    public ResponseEntity<String> addNewUser(@RequestBody UserInfo userInfo) {
        return new ResponseEntity<>(service.addUser(userInfo), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getAllTheProducts() {
        return new ResponseEntity<>(service.getProducts(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return new ResponseEntity<>(service.getProduct(id), HttpStatus.OK);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(authRequest.getUsername()), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}