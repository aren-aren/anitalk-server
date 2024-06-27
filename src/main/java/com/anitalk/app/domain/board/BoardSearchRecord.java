package com.anitalk.app.domain.board;

public record BoardSearchRecord(
        String search,
        BoardSearchType kind
) {
    public BoardSearchType kind() {
        if(kind == null) return BoardSearchType.none;

        return kind;
    }

    public enum BoardSearchType{
        title, content, both, none
    }
}
