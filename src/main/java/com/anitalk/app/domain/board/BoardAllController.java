package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.board.dto.BoardAnimationNameListRecord;
import com.anitalk.app.domain.board.dto.BoardRecord;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardAllController {
    private final BoardService boardService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<PageAnd<BoardAnimationNameListRecord>> getBoardsByUserId(@PathVariable Long userId, Pagination pagination){
        PageAnd<BoardAnimationNameListRecord> boardListRecord = boardService.getBoardsByUserId(userId, pagination);
        return ResponseEntity.ok(boardListRecord);
    }

    @GetMapping
    public ResponseEntity<PageAnd<BoardAnimationNameListRecord>> getBoards(Pagination pagination){
        PageAnd<BoardAnimationNameListRecord> boardListRecord = boardService.getBoards(pagination);
        return ResponseEntity.ok(boardListRecord);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardRecord> getBoardById(@PathVariable Long boardId) throws Exception {
        BoardRecord boardRecord = boardService.getBoardById(boardId);
        return ResponseEntity.ok(boardRecord);
    }
}
