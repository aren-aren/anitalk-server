package com.anitalk.app.attach.dto;

public record AttachRecord(
        Long id,
        String category,
        String name,
        String originName,
        String url
) {
}
