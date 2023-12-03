package com.example.plan.food.controller;

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
    public ResponseEntity<List<Map<String, Object>>> findAllFoods(){
        return new ResponseEntity<>(foodService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable int id) {
        return new ResponseEntity<>(foodService.findById(id), HttpStatus.OK);
    }

    /*------------------REVIEW----------------*/
    @GetMapping("/type")
    public ResponseEntity<List<Map<String, Object>>> findByType(@RequestParam("type") Type type) {
        return new ResponseEntity<>(foodService.findByType(type), HttpStatus.OK);
    }

    @GetMapping("/name")
    public ResponseEntity<Map<String, Object>> findByName(@RequestParam("name") String name) {
        return new ResponseEntity<>(foodService.findByName(name), HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> addFood(@RequestBody Map<String, Object> requestMap) {
        return foodService.addFood(requestMap);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> updateFood(@RequestBody Map<String, Object> requestMap, @PathVariable int id) {
        return foodService.updateFood(requestMap, id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseMessage> deleteFood(@PathVariable int  id) {
        return foodService.deleteFood(id);
    }
}
