package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.board.dto.*;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.commons.Pagination;
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
    public ResponseEntity<PageAnd<BoardListRecord>> getBoards(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            BoardOption option,
            BoardSearchRecord boardSearchRecord,
            Pagination pagination) {
        Long userId = null;
        if (user != null) userId = user.id();

        PageAnd<BoardListRecord> boardRecords = switch (option.getType()) {
            case ALL -> boardService.getBoardsByAnimationId(animationId, pagination, boardSearchRecord, userId);
            case RECOMMENDED -> boardService.getRecommendedBoards(animationId, pagination, boardSearchRecord, userId);
        };

        return ResponseEntity.ok(boardRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardRecord> getBoardById(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @PathVariable Long id) {
        Long userId = null;
        if (user != null) userId = user.id();
        BoardRecord boardRecord = boardService.getBoardById(id, userId);
        return ResponseEntity.ok(boardRecord);
    }

    @PostMapping
    public ResponseEntity<BoardRecord> addBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @RequestBody BoardAddRecord boardAddRecord,
            @RequestAttribute("ip") String ip
    ) {
        if (user != null) {
            boardAddRecord = new BoardAddRecord(
                    boardAddRecord.title(),
                    boardAddRecord.content(),
                    null,
                    null,
                    null,
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
            @RequestBody BoardAddRecord record
    ) throws Exception {
        if (user != null) {
            record = new BoardAddRecord(
                    record.title(),
                    record.content(),
                    record.nickname(),
                    record.password(),
                    null,
                    user.id(),
                    null,
                    record.attaches()
            );
        }

        BoardRecord putBoard = boardService.putBoard(id, animationId, record);
        return ResponseEntity.ok(putBoard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @RequestBody BoardWriterRecord boardWriterRecord,
            @PathVariable Long animationId,
            @PathVariable Long id
    ) throws Exception {
        if(user != null){
            boardWriterRecord = new BoardWriterRecord(
                    user.id(),
                    null,
                    null
            );
        } else if (boardWriterRecord == null || !boardWriterRecord.validate()){
            throw new Exception("작성자 정보가 잘못되었습니다.");
        }

        boardService.deleteBoard(animationId, id, boardWriterRecord);
        return ResponseEntity.ok("deleted : " + id);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<BoardLikeRecord> likeBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @PathVariable Long id) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        BoardLikeRecord boardLikeRecord = boardService.likeBoard(user.id(), id);
        return ResponseEntity.ok(boardLikeRecord);
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<BoardLikeRecord> unlikeBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @PathVariable Long id) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        BoardLikeRecord boardLikeRecord = boardService.unLikeBoard(user.id(), id);
        return ResponseEntity.ok(boardLikeRecord);
    }
}
