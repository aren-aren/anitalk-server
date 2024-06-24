package com.anitalk.app.domain.user.dto;

import com.anitalk.app.domain.user.UserEntity;

public record UsernameRecord (
        Long id,
        String nickname
){
    public static UsernameRecord of(UserEntity user){
        return new UsernameRecord(
                user.getId(),
                user.getNickname()
        );
    }
}
