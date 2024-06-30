package com.anitalk.app.domain.comment.dto;

import com.anitalk.app.domain.comment.CommentEntity;
import com.anitalk.app.domain.user.UserEntity;

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

    public static CommentAddRecord putUser(CommentAddRecord record, Long userId) {
        return new CommentAddRecord(
                userId,
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
                .user(UserEntity.builder().id(userId()).build())
                .content(content())
                .nickname(nickname())
                .password(password())
                .refId(parent())
                .step(step())
                .depth(depth())
                .build();
    }

    public CommentEntity toEntity(UserEntity user) {
        return CommentEntity.builder()
                .user(user)
                .content(content())
                .nickname(nickname())
                .password(password())
                .refId(parent())
                .step(step())
                .depth(depth())
                .build();
    }
}
