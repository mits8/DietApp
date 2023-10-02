/*
package com.example.plan.controller.restImpl;

import com.example.plan.PlanUtils;
import com.example.plan.constants.PlanConstants;
import com.example.plan.controller.rest.UserRest;
import com.example.plan.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> singUp(Map<String, String> requestMap) {
        try{
            return userService.singUp(requestMap);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try{
            return userService.login(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return PlanUtils.getResponseEntity(PlanConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}*/
