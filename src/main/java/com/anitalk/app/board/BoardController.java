package com.anitalk.app.board;

import com.anitalk.app.board.dto.BoardAddRecord;
import com.anitalk.app.board.dto.BoardListRecord;
import com.anitalk.app.board.dto.BoardRecord;
import com.anitalk.app.user.dto.AuthenticateUserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animations/{animationId}/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardListRecord>> getBoards(@PathVariable Long animationId){
        List<BoardListRecord> boardRecords = boardService.getBoards(animationId);
        return ResponseEntity.ok(boardRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardRecord> getBoardById(@PathVariable Long animationId, @PathVariable Long id){
        BoardRecord boardRecord = boardService.getBoardById(animationId, id);
        return ResponseEntity.ok(boardRecord);
    }

    @PostMapping
    public ResponseEntity<BoardRecord> addBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @RequestBody BoardAddRecord boardAddRecord
    ){
        if(user != null) {
            boardAddRecord = new BoardAddRecord(
                    boardAddRecord.title(),
                    boardAddRecord.content(),
                    boardAddRecord.nickname(),
                    boardAddRecord.password(),
                    user.id(),
                    boardAddRecord.category()
            );
        }
        BoardRecord boardRecord = boardService.addBoard(animationId, boardAddRecord);
        return ResponseEntity.ok(boardRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardRecord> putBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @PathVariable Long id,
            @RequestBody BoardRecord record
    ){
        if(user == null) return ResponseEntity.badRequest().build();

        BoardAddRecord boardAddRecord = new BoardAddRecord(
                record.title(),
                record.content(),
                null,
                null,
                user.id(),
                null
        );
        BoardRecord putBoard = boardService.putBoard(id, animationId, boardAddRecord);
        return ResponseEntity.ok(putBoard);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @PathVariable Long id ){
        if(user == null){
            return ResponseEntity.badRequest().build();
        }

        boardService.likeBoard(user.id(), id);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{id}/unlike")
    public ResponseEntity<String> unlikeBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @PathVariable Long id ){
        if(user == null){
            return ResponseEntity.badRequest().build();
        }

        boardService.unLikeBoard(user.id(), id);
        return ResponseEntity.ok("success");
    }
}
