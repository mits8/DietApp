package com.example.plan.food.controller;

import com.example.plan.dto.food.FoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.food.service.FoodService;
import com.example.plan.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/findAll")
    public ResponseEntity<List<FoodDTO>> findAll(){
        return new ResponseEntity<>(foodService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<FoodDTO> findById(@PathVariable int id) {
        return new ResponseEntity<>(foodService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/type")
    public ResponseEntity<List<FoodDTO>> findByType(@RequestParam("type") Type type) {
        return new ResponseEntity<>(foodService.findByType(type), HttpStatus.OK);
    }

    @GetMapping("/name")
    public ResponseEntity<FoodDTO> findByName(@RequestParam("name") String name) {
        return new ResponseEntity<>(foodService.findByName(name), HttpStatus.OK);
    }

    @PostMapping("/addFood")
    @PreAuthorize("hasRole('ROLE_ADMIN')")

    public ResponseEntity<ResponseMessage> saveFood(@RequestBody Map<String, String> requestMap) {
        return foodService.addFood(requestMap);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> updateCustomer(@RequestBody Map<String, String> requestMap, @PathVariable int id) {
        return foodService.updateFood(requestMap, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCustomer(@PathVariable int  id) {
        return foodService.deleteFood(id);
    }
}
