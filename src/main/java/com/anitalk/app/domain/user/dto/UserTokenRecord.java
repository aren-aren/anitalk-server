package com.anitalk.app.domain.user.dto;

public record UserTokenRecord(
        UserRecord user,
        JwtToken token
) {
}
