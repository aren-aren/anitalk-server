package com.anitalk.app.domain.comment.dto;

public record CommentPutRecord(
        Long id,
        Long userId,
        String content
) {
}
