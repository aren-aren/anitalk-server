package com.anitalk.app.domain.comment;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.commons.StringResult;
import com.anitalk.app.domain.comment.dto.CommentAddRecord;
import com.anitalk.app.domain.comment.dto.CommentPutRecord;
import com.anitalk.app.domain.comment.dto.CommentRecord;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.commons.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/{boardId}/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<PageAnd<CommentRecord>> getComments(@PathVariable Long boardId, Pagination pagination){
        PageAnd<CommentRecord> comments = commentService.getComments(boardId, pagination);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentRecord> addComments(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long boardId,
            @RequestBody CommentAddRecord commentAddRecord,
            @RequestAttribute("ip") String ip
    ) throws Exception {
        if(user != null){
            commentAddRecord = CommentAddRecord.putUser(commentAddRecord, user.id());
        } else if(commentAddRecord.nickname() == null){
            throw new Exception("닉네임이 null입니다.");
        }
        CommentRecord comment = commentService.addComment(boardId, commentAddRecord, ip);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<StringResult> deleteComment(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long boardId,
            @PathVariable Long id ) throws Exception {
        if(user == null) throw new Exception("로그인이 필요합니다.");

        commentService.deleteComment(user.id(), id);
        return ResponseEntity.ok(new StringResult("deleted : " + id));
    }
}
