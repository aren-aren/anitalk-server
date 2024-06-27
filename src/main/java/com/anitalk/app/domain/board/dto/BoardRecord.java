package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardCategory;
import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.domain.board.LikeEntity;
import com.anitalk.app.utils.DateManager;
import com.anitalk.app.utils.IpFormatter;

public record BoardRecord(
        Long id,
        Long animationId,
        String title,
        String content,
        Long hit,
        String writeDate,
        String modifyDate,
        String ip,
        String nickname,
        String password,
        Long userId,
        String category,
        String animationName,
        BoardLikeRecord like
) {
    public BoardRecord{
        if(category != null) BoardCategory.valueOf(category);
    }

    public static BoardRecord of(BoardEntity boardEntity, LikeEntity likeEntity){
        String ip = IpFormatter.format(boardEntity.getIp());

        String nickname = boardEntity.getNickname() == null ? boardEntity.getUser().getNickname() : boardEntity.getNickname();

        return new BoardRecord(
                boardEntity.getId(),
                boardEntity.getAnimation().getId(),
                boardEntity.getTitle(),
                boardEntity.getContent(),
                boardEntity.getHit(),
                boardEntity.getWriteDate(),
                boardEntity.getModifyDate(),
                ip,
                nickname,
                boardEntity.getPassword(),
                boardEntity.getUser().getId(),
                boardEntity.getCategory().toString(),
                boardEntity.getAnimation().getName(),
                new BoardLikeRecord(
                        boardEntity.getLike().size(),
                        boardEntity.getLike().contains(likeEntity)
                )
        );
    }

    public BoardEntity toEntity(){
        return BoardEntity.builder()
                .animationId(animationId())
                .title(title())
                .category(category())
                .content(content())
                .ip(ip())
                .nickname(nickname())
                .password(password())
                .userId(userId())
                .build();
    }

    public void putEntity(BoardEntity entity) {
        if(title() != null){
            entity.setTitle(title());
        }
        if(content() != null){
            entity.setContent(content());
        }
        entity.setModifyDate(DateManager.nowDateTime());
    }
}
