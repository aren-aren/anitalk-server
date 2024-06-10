package com.anitalk.app.board.dto;

import com.anitalk.app.board.BoardEntity;

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
}
