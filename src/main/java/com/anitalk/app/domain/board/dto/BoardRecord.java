package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardCategory;
import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.utils.DateManager;

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
        String category
) {
    public BoardRecord{
        if(category != null) BoardCategory.valueOf(category);
    }

    public static BoardRecord of(BoardEntity entity){
        return new BoardRecord(
                entity.getId(),
                entity.getAnimationId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getHit(),
                entity.getWriteDate(),
                entity.getModifyDate(),
                entity.getIp(),
                entity.getNickname(),
                entity.getPassword(),
                entity.getUserId(),
                entity.getCategory().toString()
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
