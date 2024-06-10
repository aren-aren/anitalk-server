package com.anitalk.app.user.dto;

import com.anitalk.app.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public record JoinUserRecord(
        String email,
        String nickname,
        String password
) {
    public UserEntity toEntity(PasswordEncoder encoder){
        return UserEntity.builder()
                .email(email())
                .nickname(nickname())
                .password(encoder.encode(password()))
                .build();
    }
}
