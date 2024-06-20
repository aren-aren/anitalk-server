package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardEntity;
import org.hibernate.Hibernate;

public record BoardListRecord(
        Long id,
        Long animationId,
        String title,
        Long hit,
        String writeDate,
        String ip,
        String nickname,
        Long userId,
        String category,
        Integer likes
) {
    public static BoardListRecord of(BoardEntity entity){
        String[] ips = entity.getIp().split("\\.");
        String ip = entity.getIp();
        if(ips.length > 1){
            ip = ips[0] + "." + ips[1];
        }

        return new BoardListRecord(
                entity.getId(),
                entity.getAnimation().getId(),
                entity.getTitle(),
                entity.getHit(),
                entity.getWriteDate(),
                entity.getModifyDate(),
                ip,
                entity.getUserId(),
                entity.getContent(),
                Hibernate.size(entity.getLike())
        );
    }
}
