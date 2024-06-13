package com.anitalk.app.user.dto;

public record UserTokenRecord(
        UserRecord user,
        JwtToken token
) {
}
