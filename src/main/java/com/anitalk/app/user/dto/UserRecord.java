package com.anitalk.app.user.dto;

import com.anitalk.app.user.UserEntity;

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
