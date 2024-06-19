package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.animation.AnimationEntity;
import com.anitalk.app.domain.attach.AttachManager;
import com.anitalk.app.domain.board.dto.*;
import com.anitalk.app.domain.user.UserEntity;
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

    public PageAnd<BoardListRecord> getBoards(Long animationId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(),Sort.by(Sort.Order.desc("writeDate")));
        return new PageAnd<>(boardRepository.findAllByAnimationIdAndDeletedIsFalse(animationId, pageable).map(BoardListRecord::of));
    }

    public PageAnd<BoardAnimationNameListRecord> getBoards(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(),Sort.by(Sort.Order.desc("writeDate")));
        return new PageAnd<>(boardRepository.findAllByDeleted(false, pageable).map(BoardAnimationNameListRecord::of));
    }

    public BoardRecord getBoardById(Long animationId, Long id) {
        BoardEntity entity = boardRepository.findByIdAndAnimationIdAndDeletedIsFalse(id, animationId).orElseThrow();

        return BoardRecord.of(entity);
    }

    public BoardRecord getBoardById(Long boardId) {
        BoardEntity entity = boardRepository.findByIdAndDeletedIsFalse(boardId).orElseThrow();

        return BoardRecord.of(entity);
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

        return BoardRecord.of(entity);
    }

    public BoardRecord putBoard(Long id, Long animationId, BoardAddRecord record) {
        BoardEntity entity = boardRepository.findByIdAndAnimationIdAndDeletedIsFalse(id, animationId).orElseThrow();
        record.putEntity(entity);

        entity = boardRepository.save(entity);
        return BoardRecord.of(entity);
    }

    public void likeBoard(Long userId, Long boardId) {
        LikeEntity like = new LikeEntity(userId, boardId);
        UserEntity user = new UserEntity();
        user.setId(userId);
        BoardEntity board = new BoardEntity();
        board.setId(boardId);

        like.setUser(user);
        like.setBoard(board);
        likeRepository.save(like);
    }

    public void unLikeBoard(Long userId, Long boardId) {
        LikeEntityId likeId = new LikeEntityId(userId, boardId);
        LikeEntity like = likeRepository.findById(likeId).orElseThrow();
        likeRepository.delete(like);
    }

    public PageAnd<BoardAnimationNameListRecord> getBoardsByUserId(Long userId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(),Sort.by(Sort.Order.desc("writeDate")));
        Page<BoardEntity> boardEntities = boardRepository.findAllByUserIdAndDeletedIsFalse(userId, pageable);

        return new PageAnd<>(boardEntities.map(BoardAnimationNameListRecord::of));
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

}
