package com.anitalk.app.domain.comment;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.domain.board.BoardRepository;
import com.anitalk.app.domain.board.dto.BoardListRecord;
import com.anitalk.app.domain.comment.dto.CommentAddRecord;
import com.anitalk.app.domain.comment.dto.CommentBoardRecord;
import com.anitalk.app.domain.comment.dto.CommentPutRecord;
import com.anitalk.app.domain.comment.dto.CommentRecord;
import com.anitalk.app.domain.notification.NoticeSender;
import com.anitalk.app.domain.notification.NoticeType;
import com.anitalk.app.domain.user.UserEntity;
import com.anitalk.app.domain.user.UserRepository;
import com.anitalk.app.domain.user.dto.UsernameRecord;
import com.anitalk.app.utils.DateManager;
import com.anitalk.app.commons.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final NoticeSender noticeSender;
    private final UserRepository userRepository;

    public PageAnd<CommentRecord> getComments(Long boardId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
        Page<CommentEntity> comments = commentRepository.findAllByBoardIdOrderByRefIdAscStepAsc(boardId, pageable);
        return new PageAnd<>(comments.map(CommentRecord::of));
    }

    @Transactional
    public CommentRecord addComment(Long boardId, CommentAddRecord commentAddRecord, String ip) {
        if(commentAddRecord.parent() != null){
            //대댓글이면 업데이트
            commentAddRecord = updateCommentsStep(commentAddRecord);
        }
        CommentEntity entity;
        if(commentAddRecord.userId() != null){
            UserEntity user = userRepository.findById(commentAddRecord.userId()).orElseThrow();
            entity = commentAddRecord.toEntity(user);
        } else {
            entity = commentAddRecord.toEntity();
        }

        BoardEntity board = boardRepository.findById(boardId).orElseThrow();

        entity.setIp(ip);
        entity.setBoard(board);
        entity.setWriteDate(DateManager.nowDateTime());
        entity.setIsDeleted(false);

        entity = commentRepository.save(entity);

        if(entity.getRefId() == null){
            entity.setRefId(entity.getId());
            entity = commentRepository.save(entity);
        }

        if(board.getUser() == null) return CommentRecord.of(entity);

        if(entity.getRefId().equals(entity.getId())){
            noticeSender.sendNotice(
                    new UsernameRecord(entity.getUser().getId(), entity.getNickname()),
                    board.getUser().getId(),
                    NoticeType.BOARD,
                    BoardListRecord.of(board, null),
                    CommentBoardRecord.of(entity)
            );
        } else {
            CommentEntity parentEntity = commentRepository.findById(entity.getRefId()).get();
            if(parentEntity.getUser().getId() == null) return CommentRecord.of(entity);

            noticeSender.sendNotice(
                    new UsernameRecord(entity.getId(), entity.getNickname()),
                    parentEntity.getUser().getId(),
                    NoticeType.COMMENT,
                    CommentBoardRecord.of(parentEntity),
                    CommentBoardRecord.of(entity)
            );
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
        Optional<CommentEntity> parentNextComment =
                commentRepository.findTopByBoardIdAndStepGreaterThanAndDepthOrderByWriteDate(parentComment.getBoard().getId(), parentComment.getStep(), parentComment.getDepth());
        Long start = parentComment.getStep();
        Long end = 100_000L;
        if(parentNextComment.isPresent()){
            end = parentNextComment.get().getStep();
        }

        Optional<CommentEntity> upperComment =
                commentRepository.findTopByBoardIdAndRefIdAndDepthAndStepBetweenOrderByStepDesc(parentComment.getBoard().getId(), parentComment.getRefId(), parentComment.getDepth()+1, start, end);

        Long refId = parentComment.getRefId();
        long step = parentComment.getStep() + 1;
        if(upperComment.isPresent()){
            step = upperComment.get().getStep() + 1;
        }
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

    public CommentBoardRecord getCommentBoardRecordById(Long id) {
        return CommentBoardRecord.of(commentRepository.findById(id).orElseThrow());
    }

    public void deleteComment(Long userId, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow();
        commentRepository.delete(comment);
    }
}
