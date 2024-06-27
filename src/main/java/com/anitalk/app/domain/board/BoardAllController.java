package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.board.dto.BoardListRecord;
import com.anitalk.app.domain.board.dto.BoardRecord;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardAllController {
    private final BoardService boardService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<PageAnd<BoardListRecord>> getBoardsByUserId(@PathVariable Long userId, Pagination pagination){
        PageAnd<BoardListRecord> boardListRecord = boardService.getBoardsByUserId(userId, pagination);
        return ResponseEntity.ok(boardListRecord);
    }

    @GetMapping
    public ResponseEntity<PageAnd<BoardListRecord>> getBoards(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            Pagination pagination){
        Long userId = null;
        if(user != null) userId = user.id();
        PageAnd<BoardListRecord> boardListRecord = boardService.getBoardsAll(pagination, userId);
        return ResponseEntity.ok(boardListRecord);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardRecord> getBoardById(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long boardId) {
        Long userId = null;
        if(user != null) userId = user.id();
        BoardRecord boardRecord = boardService.getBoardById(boardId, userId);
        return ResponseEntity.ok(boardRecord);
    }
}
