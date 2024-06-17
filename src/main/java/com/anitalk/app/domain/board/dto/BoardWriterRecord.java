package com.anitalk.app.domain.board.dto;

public record BoardWriterRecord(
        String nickname,
        String password
) {
    public boolean validate() {
        return nickname() != null && password() != null;
    }
}
