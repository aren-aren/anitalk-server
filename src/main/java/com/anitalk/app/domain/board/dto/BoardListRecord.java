package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardEntity;

public record BoardListRecord(
        Long id,
        Long animationId,
        String title,
        Long hit,
        String writeDate,
        String ip,
        String nickname,
        Long userId,
        String category
//        Integer likes
) {
    public static BoardListRecord of(BoardEntity entity){
        return new BoardListRecord(
                entity.getId(),
                entity.getAnimationId(),
                entity.getTitle(),
                entity.getHit(),
                entity.getWriteDate(),
                entity.getModifyDate(),
                entity.getIp(),
                entity.getUserId(),
                entity.getContent()
//                Hibernate.size(entity.getLike())
        );
    }
}
