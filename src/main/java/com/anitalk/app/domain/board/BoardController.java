package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.commons.StringResult;
import com.anitalk.app.domain.board.dto.*;
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
    public ResponseEntity<PageAnd<BoardListRecord>> getBoards(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            BoardOption option,
            Pagination pagination) {
        Long userId = null;
        if (user != null) userId = user.id();

        PageAnd<BoardListRecord> boardRecords = switch (option.getType()){
            case ALL -> boardService.getBoards(animationId, pagination, userId);
            case RECOMMENDED -> boardService.getRecommendedBoards(animationId, pagination, userId);
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
        BoardRecord boardRecord = boardService.getBoardById(animationId, id, userId);
        return ResponseEntity.ok(boardRecord);
    }

    @PostMapping
    public ResponseEntity<BoardRecord> addBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @RequestBody BoardAddRecord boardAddRecord,
            HttpServletRequest request
    ) {
        String ip = (String) request.getAttribute("ip");

        if (user != null) {
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
    ) {
        if (user == null) return ResponseEntity.badRequest().build();

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @PathVariable Long id
    ) throws Exception {
        if (user == null) throw new Exception("로그인이 필요합니다");

        boardService.deleteBoard(user.id(), animationId, id);
        return ResponseEntity.ok("deleted : " + id);
    }

    @DeleteMapping("/{id}/anonymous")
    public ResponseEntity<StringResult> deleteBoard(
            @RequestBody BoardWriterRecord boardWriterRecord,
            @PathVariable Long animationId,
            @PathVariable Long id
    ) throws Exception {
        if (boardWriterRecord == null || !boardWriterRecord.validate()) {
            throw new Exception("게시글 작성자 정보가 유효하지 않습니다.");
        }

        boardService.deleteBoard(id, animationId, boardWriterRecord);
        return ResponseEntity.ok(new StringResult("deleted : " + id));
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
