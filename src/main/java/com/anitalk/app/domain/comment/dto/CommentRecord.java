package com.anitalk.app.domain.comment.dto;

import com.anitalk.app.domain.comment.CommentEntity;

public record CommentRecord(
        Long id,
        Long userId,
        Long boardId,
        String content,
        String writeDate,
        String nickname,
        String password,
        String ip,
        Long refId,
        Long depth,
        Long step
) {
    public static CommentRecord of(CommentEntity entity){
        return new CommentRecord(
                entity.getId(),
                entity.getUserId(),
                entity.getBoard().getId(),
                entity.getContent(),
                entity.getWriteDate(),
                entity.getNickname(),
                entity.getPassword(),
                entity.getIp(),
                entity.getRefId(),
                entity.getDepth(),
                entity.getStep()
        );
    }
}
