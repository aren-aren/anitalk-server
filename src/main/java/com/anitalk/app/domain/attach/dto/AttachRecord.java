package com.anitalk.app.domain.attach.dto;

public record AttachRecord(
        Long id,
        String category,
        String name,
        Long parentId,
        String originName,
        String url
) {
}
