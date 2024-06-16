package com.anitalk.app.domain.user.dto;

public record EmailDuplicationRecord (
        String email,
        boolean exist
){
}
