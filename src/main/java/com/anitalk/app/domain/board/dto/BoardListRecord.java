package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.domain.board.LikeEntity;
import com.anitalk.app.domain.notification.NoticeContent;
import com.anitalk.app.utils.IpFormatter;

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
        String ip = IpFormatter.format(boardEntity.getIp());

        String nickname = boardEntity.getNickname() == null ? boardEntity.getUser().getNickname() : boardEntity.getNickname();

        return new BoardListRecord(
                boardEntity.getId(),
                boardEntity.getAnimation().getId(),
                boardEntity.getTitle(),
                boardEntity.getHit(),
                boardEntity.getWriteDate(),
                ip,
                nickname,
                boardEntity.getUser().getId(),
                boardEntity.getContent(),
                new BoardLikeRecord(
                        boardEntity.getLike().size(),
                        boardEntity.getLike().contains(likeEntity)
                )
        );
    }
}
