package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.board.dto.BoardAddRecord;
import com.anitalk.app.domain.board.dto.BoardListRecord;
import com.anitalk.app.domain.board.dto.BoardRecord;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.utils.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animations/{animationId}/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<PageAnd<BoardListRecord>> getBoards(@PathVariable Long animationId, Pagination pagination){
        PageAnd<BoardListRecord> boardRecords = boardService.getBoards(animationId, pagination);
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
            @RequestBody BoardAddRecord boardAddRecord,
            HttpServletRequest request
    ){
        String ip = (String)request.getAttribute("ip");

        if(user != null) {
            boardAddRecord = new BoardAddRecord(
                    boardAddRecord.title(),
                    boardAddRecord.content(),
                    null,
                    null,
                    boardAddRecord.ip(),
                    user.id(),
                    boardAddRecord.category(),
                    boardAddRecord.attaches()
            );
        }
        BoardRecord boardRecord = boardService.addBoard(animationId, boardAddRecord, ip);
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
                record.ip(),
                user.id(),
                null,
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

    @DeleteMapping("/{id}/like")
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
