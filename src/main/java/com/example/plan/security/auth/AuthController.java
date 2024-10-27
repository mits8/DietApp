package com.example.plan.security.auth;

import com.example.plan.enums.Role;
import com.example.plan.security.auth.service.AuthService;
import com.example.plan.security.config.UserInfoUserDetails;
import com.example.plan.security.config.UserInfoUserDetailsService;
import com.example.plan.security.config.filter.JwtService;
import com.example.plan.user.entity.UserInfo;
import com.example.plan.user.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {

    String jwt;

    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInfoUserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserInfoRepository userInfoRepository;



    @PostMapping("/generateToken")
    public ResponseEntity<String> generateToken(AuthRequest authRequest) {
        Authentication authentication = authenticate(authRequest.getEmail(), authRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role;

        if (userDetails.getAuthorities() != null) {
            role = userDetails.getAuthorities().toString();
        } else {
            role = Role.CUSTOMER.toString();
        }
        authRequest.setRole(role);
        return authService.generateToken(authRequest);
    }
    /*@PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody AuthRequest authRequest){
        Authentication authentication = authenticate(authRequest.getEmail(), authRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role;

        if (userDetails.getAuthorities() != null) {
            role = userDetails.getAuthorities().toString();
        } else {
            role = Role.MODERATOR.toString();
        }

        String token = jwtService.generateToken(authRequest.getEmail(), role);

        return authService.login(authRequest);
    }*/
    private Authentication authenticate(String email, String password) {
        System.out.println(email + "--------++--------" + password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        System.out.println("Sign in user details: " + userDetails);
        if (userDetails == null) {
            System.out.println("Sign in user details - null: " + userDetails);
            throw new BadCredentialsException("Invalid email or password");
        }
        UserInfo userInfo = userInfoRepository.findUserByEmail(email);

        if (!passwordEncoder.matches(password,userInfo.getPassword())) {
            System.out.println("Sign in user details - wrong password" + userDetails);
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {

        Authentication authentication = authenticate(authRequest.getEmail(), authRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role;

        if (userDetails.getAuthorities() != null) {
            role = userDetails.getAuthorities().toString();
        } else {
            role = Role.CUSTOMER.toString();
        }
        String token = jwtService.generateToken(authRequest.getEmail(), authRequest.getRole());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setEmail(authRequest.getEmail());
        authResponse.setToken(token);
        authResponse.setRole(role);
        authRequest.setRole(role);
        //userInfoDetails.setJwt(token);
        return ResponseEntity.ok(authResponse);
    }
    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    public ResponseEntity<String> logout (@RequestBody LogoutRequest logoutRequest){
        return authService.logout(logoutRequest);
    }
    @GetMapping("/getToken")
    public String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object userDetails = authentication.getPrincipal();
        }
        return null;
    }
}
