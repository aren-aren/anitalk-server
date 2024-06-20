package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.domain.board.LikeEntity;
import com.anitalk.app.domain.board.LikeEntityId;
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
        BoardLikeRecord like
) {
    public static BoardAnimationNameListRecord of(BoardEntity boardEntity, LikeEntity likeEntity){
        return new BoardAnimationNameListRecord(
                boardEntity.getId(),
                boardEntity.getAnimation().getId(),
                boardEntity.getAnimation().getName(),
                boardEntity.getTitle(),
                boardEntity.getHit(),
                boardEntity.getWriteDate(),
                boardEntity.getIp(),
                boardEntity.getNickname(),
                boardEntity.getUserId(),
                boardEntity.getCategory().name(),
                new BoardLikeRecord(
                        boardEntity.getLike().size(),
                        boardEntity.getLike().contains(likeEntity)
                )
        );
    }
}
