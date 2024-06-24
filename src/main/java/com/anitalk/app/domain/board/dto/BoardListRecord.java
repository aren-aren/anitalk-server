package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.domain.board.LikeEntity;
import com.anitalk.app.domain.notification.NoticeContent;

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
        BoardLikeRecord like
) implements NoticeContent {
    public static BoardListRecord of(BoardEntity boardEntity, LikeEntity likeEntity){
        String[] ips = boardEntity.getIp().split("\\.");
        String ip = boardEntity.getIp();
        if(ips.length > 1){
            ip = ips[0] + "." + ips[1];
        }

        return new BoardListRecord(
                boardEntity.getId(),
                boardEntity.getAnimation().getId(),
                boardEntity.getTitle(),
                boardEntity.getHit(),
                boardEntity.getWriteDate(),
                ip,
                boardEntity.getNickname(),
                boardEntity.getUserId(),
                boardEntity.getContent(),
                new BoardLikeRecord(
                        boardEntity.getLike().size(),
                        boardEntity.getLike().contains(likeEntity)
                )
        );
    }
}
