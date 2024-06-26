package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.utils.DateManager;

import java.util.Set;

public record BoardAddRecord(
        String title,
        String content,
        String nickname,
        String password,
        String ip,
        Long userId,
        String category,
        Set<String> attaches
) {

    public BoardEntity toEntity(){
        return BoardEntity.builder()
                .title(title())
                .content(content())
                .nickname(nickname())
                .password(password())
                .ip(ip())
                .userId(userId())
                .category(category())
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
