package com.anitalk.app.board.dto;

import com.anitalk.app.board.BoardEntity;
import com.anitalk.app.utils.DateManager;

public record BoardAddRecord(
        String title,
        String content,
        String nickname,
        String password,
        Long userId,
        String category
) {

    public BoardEntity toEntity(){
        return BoardEntity.builder()
                .title(title())
                .content(content())
                .nickname(nickname())
                .password(password())
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
