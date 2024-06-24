package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.animation.AnimationEntity;
import com.anitalk.app.domain.attach.AttachManager;
import com.anitalk.app.domain.board.dto.*;
import com.anitalk.app.domain.user.UserEntity;
import com.anitalk.app.domain.user.UserRepository;
import com.anitalk.app.utils.DateManager;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final AttachManager attachManager;
    private final UserRepository userRepository;

    public PageAnd<BoardListRecord> getBoards(Long animationId, Pagination pagination, Long userId) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(),Sort.by(Sort.Order.desc("writeDate")));
        Page<BoardListRecord> boards = boardRepository.findAllByAnimationIdAndDeletedIsFalse(animationId, pageable)
                .map(board -> BoardListRecord.of(board, new LikeEntity(userId , board.getId())));
        return new PageAnd<>(boards);
    }

    public PageAnd<BoardAnimationNameListRecord> getBoards(Pagination pagination, Long userId) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(),Sort.by(Sort.Order.desc("writeDate")));
        Page<BoardAnimationNameListRecord> boards = boardRepository.findAllByDeleted(false, pageable)
                .map(board -> BoardAnimationNameListRecord.of(board, new LikeEntity(userId, board.getId())));
        return new PageAnd<>(boards);
    }

    public BoardRecord getBoardById(Long animationId, Long id, Long userId) {
        BoardEntity entity = boardRepository.findByIdAndAnimationIdAndDeletedIsFalse(id, animationId).orElseThrow();
        entity.setHit(entity.getHit() + 1);
        entity = boardRepository.save(entity);

        return BoardRecord.of(entity, new LikeEntity(userId, entity.getId()));
    }

    public BoardRecord getBoardById(Long boardId, Long userId) {
        BoardEntity entity = boardRepository.findByIdAndDeletedIsFalse(boardId).orElseThrow();
        entity.setHit(entity.getHit() + 1);
        entity = boardRepository.save(entity);

        return BoardRecord.of(entity, new LikeEntity(userId, entity.getId()));
    }

    public BoardRecord addBoard(Long animationId, BoardAddRecord board, String ip) {
        BoardEntity entity = board.toEntity();
        AnimationEntity animationEntity = new AnimationEntity();
        animationEntity.setId(animationId);
        entity.setAnimation(animationEntity);
        entity.setHit(0L);
        entity.setDeleted(false);
        entity.setWriteDate(DateManager.nowDateTime());
        entity.setIp(ip);

        entity = boardRepository.save(entity);

        if(board.attaches() != null){
            attachManager.connectAttaches("board", entity.getId(), board.attaches());
        }

        return BoardRecord.of(entity, new LikeEntity(entity.getUserId(), entity.getId()));
    }

    public BoardRecord putBoard(Long id, Long animationId, BoardAddRecord record) {
        BoardEntity entity = boardRepository.findByIdAndAnimationIdAndDeletedIsFalse(id, animationId).orElseThrow();
        record.putEntity(entity);

        entity = boardRepository.save(entity);
        return BoardRecord.of(entity, new LikeEntity(entity.getUserId(), entity.getId()));
    }

    public BoardLikeRecord likeBoard(Long userId, Long boardId) {
        LikeEntity like = new LikeEntity(userId, boardId);
        UserEntity user = userRepository.findById(userId).orElseThrow();
        BoardEntity board = boardRepository.findById(boardId).orElseThrow();

        like.setUser(user);
        like.setBoard(board);
        likeRepository.save(like);

        return BoardLikeRecord.of(board, like);
    }

    public BoardLikeRecord unLikeBoard(Long userId, Long boardId) {
        LikeEntityId likeId = new LikeEntityId(userId, boardId);
        LikeEntity like = likeRepository.findById(likeId).orElseThrow();
        likeRepository.delete(like);

        BoardEntity board = boardRepository.findById(boardId).orElseThrow();
        return BoardLikeRecord.of(board, like);
    }

    public PageAnd<BoardAnimationNameListRecord> getBoardsByUserId(Long userId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(),Sort.by(Sort.Order.desc("writeDate")));
        Page<BoardAnimationNameListRecord> boardEntities = boardRepository.findAllByUserIdAndDeletedIsFalse(userId, pageable)
                .map(board -> BoardAnimationNameListRecord.of(board, new LikeEntity(userId, board.getId())));

        return new PageAnd<>(boardEntities);
    }

    public void deleteBoard(Long userId, Long animationId, Long boardId) {
        BoardEntity board = boardRepository.findByUserIdAndAnimationIdAndIdAndDeletedIsFalse(userId, animationId, boardId).orElseThrow();
        board.setDeleted(true);
        boardRepository.save(board);
    }

    public void deleteBoard(Long boardId, Long animationId, BoardWriterRecord boardWriterRecord) {
        BoardEntity board =
                boardRepository.findByNicknameAndPasswordAndAnimationIdAndIdAndDeletedIsFalse(
                    boardWriterRecord.nickname(),
                    boardWriterRecord.password(),
                    animationId,
                    boardId)
                .orElseThrow();
        board.setDeleted(true);
        boardRepository.save(board);
    }

    public BoardListRecord getBoardListRecordById(Long id) {
        return BoardListRecord.of(boardRepository.findById(id).orElseThrow(), null);
    }
}
