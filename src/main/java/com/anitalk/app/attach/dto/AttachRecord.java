package com.anitalk.app.attach.dto;

public record AttachRecord(
        Long id,
        Long boardId,
        String name,
        String originName,
        String url
) {
}
