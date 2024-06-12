package com.anitalk.app.comment.dto;

public record CommentPutRecord(
        Long id,
        Long userId,
        String content
) {
}
