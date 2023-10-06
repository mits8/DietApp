package com.example.plan.security.config;

import com.example.plan.enums.Role;
import com.example.plan.user.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserInfoUserDetails implements UserDetails {


    private String name;
    private String password;
    private Role role;
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();


    public UserInfoUserDetails(UserInfo userInfo) {
        name=userInfo.getEmail();
        password=userInfo.getPassword();
        role=userInfo.getRole();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
