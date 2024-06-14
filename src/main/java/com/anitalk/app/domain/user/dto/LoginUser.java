package com.anitalk.app.domain.user.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class LoginUser implements UserDetails {
    private final AuthenticateUserRecord userRecord;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return userRecord.password();
    }

    @Override
    public String getUsername() {
        return userRecord.email();
    }

    public AuthenticateUserRecord getUserRecord() {
        return userRecord;
    }
}
