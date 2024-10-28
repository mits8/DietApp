package com.example.plan.product.controller;

import com.example.plan.product.entity.Product;
import com.example.plan.product.service.ProductService;
import com.example.plan.security.config.filter.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping("/getAll")
    public ResponseEntity<Product> getAll() {
        return ResponseEntity.ok(productService.count());
    }


}