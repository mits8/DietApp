package com.example.plan.entity;

import com.example.plan.enums.Role;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    private UserInfo userInfo;

    private UserInfo userInfo1;


    @BeforeEach
    public void setUp() {
        // Initialize a UserInfo instance with sample data before each test
        userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setName("Dimitris");
        userInfo.setEmail("dim@gmail.com");
        userInfo.setPassword("123");
        userInfo.setLoggedIn(false);
        userInfo.setRole(Role.USER);

        userInfo1 = new UserInfo();
        userInfo1.setId(1);
        userInfo1.setName("Dimitris");
        userInfo1.setPassword("12345");
        userInfo1.setLoggedIn(false);
        userInfo1.setRole(Role.ADMIN);

    }

    @Test
    public void testEntity() {
        assertEquals(1, userInfo.getId());
        assertEquals("Dimitris", userInfo.getName());
        assertEquals("dim@gmail.com", userInfo.getEmail());
        assertEquals("123", userInfo.getPassword());
        assertFalse(userInfo.isLoggedIn());
        assertEquals(Role.USER, userInfo.getRole());


    }

    @Test
    public void nullEmail() {

        assertThrows(Exception.class, () -> {
            if (userInfo1.getEmail().equals(null)) {
                throw new Exception("Email is null");
            }
        });
    }

    @Test
    void invalidRole() {
        assertThrows(Exception.class, () -> {
            if(userInfo1.getRole() != Role.USER ){
                throw new Exception("Invalid Role!");
            };
        });
    }

    @Test
    void invalidPassword() {

        String password = "5678";
        assertThrows(Exception.class, () -> {
            if(userInfo1.getPassword() != password ){
                throw new Exception("Invalid Role!");
            };
        });
    }
}
