package com.anitalk.app.domain.comment;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.domain.board.BoardRepository;
import com.anitalk.app.domain.comment.dto.CommentAddRecord;
import com.anitalk.app.domain.comment.dto.CommentBoardRecord;
import com.anitalk.app.domain.comment.dto.CommentPutRecord;
import com.anitalk.app.domain.comment.dto.CommentRecord;
import com.anitalk.app.utils.DateManager;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public PageAnd<CommentRecord> getComments(Long boardId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
        Page<CommentEntity> comments = commentRepository.findAllByBoardIdOrderByRefIdDescStepAsc(boardId, pageable);
        return new PageAnd<>(comments.map(CommentRecord::of));
    }

    @Transactional
    public CommentRecord addComment(Long boardId, CommentAddRecord commentAddRecord) {
        if(commentAddRecord.parent() != null){
            //대댓글이면 업데이트
            commentAddRecord = updateCommentsStep(commentAddRecord);
        }

        CommentEntity entity = commentAddRecord.toEntity();
        BoardEntity board = boardRepository.findById(boardId).orElseThrow();

        entity.setBoard(board);
        entity.setWriteDate(DateManager.nowDateTime());
        entity.setIsDeleted(false);

        entity = commentRepository.save(entity);

        if(entity.getRefId() == null){
            entity.setRefId(entity.getId());
            commentRepository.save(entity);
        }

        return CommentRecord.of(entity);
    }

    public CommentRecord putComment(CommentPutRecord commentAddRecord) {
        CommentEntity entity = commentRepository.findById(commentAddRecord.id()).orElseThrow();
        entity.setContent(commentAddRecord.content());

        entity = commentRepository.save(entity);
        return CommentRecord.of(entity);
    }

    private CommentAddRecord updateCommentsStep(CommentAddRecord comment) {
        CommentEntity parentComment = commentRepository.findById(comment.parent()).orElseThrow();

        Long refId = parentComment.getRefId();
        Long step = parentComment.getStep() + 1;
        Long depth = parentComment.getDepth() + 1;

        commentRepository.updateComments(refId, step);

        return new CommentAddRecord(
                comment.userId(),
                comment.content(),
                comment.nickname(),
                comment.password(),
                refId,
                step,
                depth
        );
    }

    public PageAnd<CommentBoardRecord> getCommentsByUserId(Long userId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
        Page<CommentEntity> commentEntities = commentRepository.findAllByUserIdOrderByWriteDateDesc(userId, pageable);

        return new PageAnd<>(commentEntities.map(CommentBoardRecord::of));
    }
}
