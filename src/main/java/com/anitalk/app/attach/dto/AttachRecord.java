package com.anitalk.app.attach.dto;

public record AttachRecord(
        Long id,
        String category,
        String name,
        Long parentId,
        String originName,
        String url
) {
}
