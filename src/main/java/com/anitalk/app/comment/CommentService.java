package com.anitalk.app.comment;

import com.anitalk.app.comment.dto.CommentAddRecord;
import com.anitalk.app.comment.dto.CommentPutRecord;
import com.anitalk.app.comment.dto.CommentRecord;
import com.anitalk.app.utils.DateManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository repository;

    public List<CommentRecord> getComments(Long boardId) {
        List<CommentEntity> comments = repository.findAllByBoardIdOrderByRefIdDescStepAsc(boardId);
        return comments.stream().map(CommentRecord::of).toList();
    }

    @Transactional
    public CommentRecord addComment(Long boardId, CommentAddRecord commentAddRecord) {
        if(commentAddRecord.parent() != null){
            //대댓글이면 업데이트
            commentAddRecord = updateCommentsStep(commentAddRecord);
        }

        CommentEntity entity = commentAddRecord.toEntity();
        entity.setBoardId(boardId);
        entity.setWriteDate(DateManager.nowDateTime());
        entity.setIsDeleted(false);

        entity = repository.save(entity);

        if(entity.getRefId() == null){
            entity.setRefId(entity.getId());
            repository.save(entity);
        }

        return CommentRecord.of(entity);
    }

    public CommentRecord putComment(CommentPutRecord commentAddRecord) {
        CommentEntity entity = repository.findById(commentAddRecord.id()).orElseThrow();
        entity.setContent(commentAddRecord.content());

        entity = repository.save(entity);
        return CommentRecord.of(entity);
    }

    private CommentAddRecord updateCommentsStep(CommentAddRecord comment) {
        CommentEntity parentComment = repository.findById(comment.parent()).orElseThrow();

        Long refId = parentComment.getRefId();
        Long step = parentComment.getStep() + 1;
        Long depth = parentComment.getDepth() + 1;

        repository.updateComments(refId, step);

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

}
