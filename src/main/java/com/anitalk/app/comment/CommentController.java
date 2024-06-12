package com.anitalk.app.comment;

import com.anitalk.app.comment.dto.CommentAddRecord;
import com.anitalk.app.comment.dto.CommentPutRecord;
import com.anitalk.app.comment.dto.CommentRecord;
import com.anitalk.app.user.dto.AuthenticateUserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentRecord>> getComments(@PathVariable Long boardId){
        List<CommentRecord> comments = commentService.getComments(boardId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentRecord> addComments(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long boardId,
            @RequestBody CommentAddRecord commentAddRecord
    ){
        if(user != null){
            commentAddRecord = CommentAddRecord.putUser(commentAddRecord, user.id());
        }
        CommentRecord comment = commentService.addComment(boardId, commentAddRecord);
        return ResponseEntity.ok(comment);
    }

    @PutMapping
    public ResponseEntity<CommentRecord> putComments(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long boardId,
            @RequestBody CommentPutRecord commentRecord) throws Exception {
        if(user != null && !user.id().equals(commentRecord.userId())){
            throw new Exception("로그인한 사용자와 댓글 작성자와 다릅니다");
        }

        CommentRecord comment = commentService.putComment(commentRecord);
        return ResponseEntity.ok(comment);
    }
}
