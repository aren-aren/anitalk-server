package com.anitalk.app.domain.board.dto;

import com.anitalk.app.domain.board.BoardType;

public class BoardOption {
    private BoardType type;

    public BoardType getType() {
        if(type == null){
            return BoardType.ALL;
        }

        return type;
    }

    public void setType(BoardType type) {
        this.type = type;
    }
}
