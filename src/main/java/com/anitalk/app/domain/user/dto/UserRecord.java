package com.anitalk.app.domain.user.dto;

import com.anitalk.app.domain.user.UserEntity;

public record UserRecord(
        Long id,
        String email,
        String nickname
) {
    public static UserRecord of(UserEntity entity){
        return new UserRecord(
                entity.getId(),
                entity.getEmail(),
                entity.getNickname()
        );
    }
}
