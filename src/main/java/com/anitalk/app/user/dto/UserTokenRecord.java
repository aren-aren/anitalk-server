package com.anitalk.app.user.dto;

public record UserTokenRecord(
        UserRecord nickname,
        JwtToken token
) {
}
