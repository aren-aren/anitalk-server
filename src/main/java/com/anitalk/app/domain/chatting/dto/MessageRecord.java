package com.anitalk.app.domain.chatting.dto;

public record MessageRecord(
        String nickname,
        String content,
        String sendDate
) {
}
