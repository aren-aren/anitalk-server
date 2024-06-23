package com.anitalk.app.domain.comment.dto;

import com.anitalk.app.domain.comment.CommentEntity;

public record CommentBoardRecord(
        Long animationId,
        Long boardId,
        Long commentId,
        String animationName,
        String boardTitle,
        String content,
        String date
) {

    public static CommentBoardRecord of(CommentEntity comment){
        return new CommentBoardRecord(
                comment.getBoard().getAnimation().getId(),
                comment.getBoard().getId(),
                comment.getId(),
                comment.getBoard().getAnimation().getName(),
                comment.getBoard().getTitle(),
                comment.getContent(),
                comment.getWriteDate()
        );
    }
}
