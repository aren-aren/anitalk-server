package com.anitalk.app.domain.board.dto;

public record BoardLikeRecord(
        Integer count,
        boolean isLike
) {
}
