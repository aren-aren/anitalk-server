package com.anitalk.app.user.dto;

public record JwtToken(
        String tokenType,
        String token
) {
}
