package com.anitalk.app.domain.user.dto;

public record JwtToken(
        String tokenType,
        String token
) {
}
