package com.anitalk.app.domain.comment.dto;

import com.anitalk.app.domain.comment.CommentEntity;

public record CommentAddRecord(
        Long userId,
        String content,
        String nickname,
        String password,
        Long parent,
        Long step,
        Long depth
) {
    public CommentAddRecord{
        if(step == null) step = 0L;
        if(depth == null) depth = 0L;
    }

    public static CommentAddRecord putUser(CommentAddRecord record, Long id) {
        return new CommentAddRecord(
                id,
                record.content(),
                null,
                null,
                record.parent(),
                null,
                null
        );
    }

    public CommentEntity toEntity() {
        return CommentEntity.builder()
                .content(content())
                .nickname(nickname())
                .password(password())
                .refId(parent())
                .step(step())
                .depth(depth())
                .build();
    }
}
