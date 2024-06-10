package com.anitalk.app.user.dto;

import com.anitalk.app.user.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public record AuthenticateUserRecord(
        Long id,
        String email,
        String nickname,
        String password
) {

    public static AuthenticateUserRecord of(UserEntity entity){
        return new AuthenticateUserRecord(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getPassword()
        );
    }

    public UserEntity toEntity(PasswordEncoder encoder){
        return UserEntity.builder()
                .email(email())
                .nickname(nickname())
                .password(encoder.encode(password()))
                .build();
    }
}
