package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardEntity;
import org.hibernate.Hibernate;

public record BoardAnimationNameListRecord(
        Long id,
        Long animationId,
        String animationName,
        String title,
        Long hit,
        String writeDate,
        String ip,
        String nickname,
        Long userId,
        String category,
        Integer likes
) {
    public static BoardAnimationNameListRecord of(BoardEntity entity){
        return new BoardAnimationNameListRecord(
                entity.getId(),
                entity.getAnimation().getId(),
                entity.getAnimation().getName(),
                entity.getTitle(),
                entity.getHit(),
                entity.getWriteDate(),
                entity.getIp(),
                entity.getNickname(),
                entity.getUserId(),
                entity.getCategory().name(),
                Hibernate.size(entity.getLike())
        );
    }
}
