package com.anitalk.app.domain.comment;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.comment.dto.CommentBoardRecord;
import com.anitalk.app.domain.comment.dto.CommentRecord;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentAllController {
    private final CommentService commentService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<PageAnd<CommentBoardRecord>> getBoardsByUserId(@PathVariable Long userId, Pagination pagination){
        PageAnd<CommentBoardRecord> boardListRecord = commentService.getCommentsByUserId(userId, pagination);
        return ResponseEntity.ok(boardListRecord);
    }
}
