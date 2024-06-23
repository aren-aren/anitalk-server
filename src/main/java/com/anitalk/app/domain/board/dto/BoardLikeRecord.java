package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.domain.board.LikeEntity;

public record BoardLikeRecord(
        Integer count,
        boolean isLike
) {
    public static BoardLikeRecord of(BoardEntity board, LikeEntity like) {
        return new BoardLikeRecord(
                board.getLike().size(),
                board.getLike().contains(like)
        );
    }
}
