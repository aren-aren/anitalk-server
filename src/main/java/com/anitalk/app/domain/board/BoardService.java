package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.animation.AnimationEntity;
import com.anitalk.app.domain.attach.AttachManager;
import com.anitalk.app.domain.board.dto.*;
import com.anitalk.app.domain.user.UserEntity;
import com.anitalk.app.domain.user.UserRepository;
import com.anitalk.app.utils.DateManager;
import com.anitalk.app.commons.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final AttachManager attachManager;
    private final UserRepository userRepository;
    private final BoardHotCalculator boardHotCalculator;

    public PageAnd<BoardListRecord> getBoardsByAnimationId(Long animationId, Pagination pagination, BoardSearchRecord boardSearchRecord, Long userId) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.by(Sort.Order.desc("writeDate")));
        Page<BoardEntity> boards = switch (boardSearchRecord.kind()) {
            case title ->
                    boardRepository.findAllByAnimationIdAndTitleContainsFetchJoin(animationId, boardSearchRecord.search(), pageable);
            case content ->
                    boardRepository.findAllByAnimationIdAndContentContainsFetchJoin(animationId,  boardSearchRecord.search(), pageable);
            case both ->
                    boardRepository.findAllByAnimationIdAndBothContainsFetchJoin(animationId, boardSearchRecord.search(), pageable);
            case none -> boardRepository.findAllByAnimationIdFetchJoin(animationId, pageable);
        };


        return new PageAnd<>(
                boards.map(board -> BoardListRecord.of(board, new LikeEntity(userId, board.getId())))
        );
    }

    public PageAnd<BoardListRecord> getBoardsAll(Pagination pagination, BoardSearchRecord boardSearchRecord, Long userId) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.by(Sort.Order.desc("writeDate")));

        Page<BoardEntity> boards = switch (boardSearchRecord.kind()) {
            case title ->
                    boardRepository.findAllByTitleContainsFetchJoin(boardSearchRecord.search(), pageable);
            case content ->
                    boardRepository.findAllByContentContainsFetchJoin(boardSearchRecord.search(), pageable);
            case both ->
                    boardRepository.findAllByBothContainsFetchJoin(boardSearchRecord.search(), pageable);
            case none -> boardRepository.findAllFetchJoin(pageable);
        };

        return new PageAnd<>(
                boards.map(board -> BoardListRecord.of(board, new LikeEntity(userId, board.getId())))
        );
    }

    public BoardRecord getBoardById(Long boardId, Long userId) {
        BoardEntity entity = boardRepository.findById(boardId).orElseThrow();
        entity.setHit(entity.getHit() + 1);
        entity = boardRepository.save(entity);

        return BoardRecord.of(entity, new LikeEntity(userId, entity.getId()));
    }

    public PageAnd<BoardListRecord> getBoardsByUserId(Long userId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.by(Sort.Order.desc("writeDate")));
        Page<BoardListRecord> boardEntities = boardRepository.findAllByUserId(userId, pageable)
                .map(board -> BoardListRecord.of(board, new LikeEntity(userId, board.getId())));

        return new PageAnd<>(boardEntities);
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

        if (board.attaches() != null) {
            attachManager.connectAttaches("board", entity.getId(), board.attaches());
        }

        return BoardRecord.of(entity, new LikeEntity(entity.getUser().getId(), entity.getId()));
    }

    public BoardRecord putBoard(Long id, Long animationId, BoardAddRecord record) {
        BoardEntity entity = getBoardByUser(id, animationId, new BoardWriterRecord(record.userId(), record.nickname(), record.password()));

        record.putEntity(entity);

        if (!record.attaches().isEmpty()) {
            attachManager.PutConnectionAttaches("boards", entity.getId(), record.attaches());
        }

        entity = boardRepository.save(entity);
        return BoardRecord.of(entity, new LikeEntity(entity.getUser().getId(), entity.getId()));
    }

    public void deleteBoard(Long boardId, Long animationId, BoardWriterRecord boardWriterRecord) {
        BoardEntity board = getBoardByUser(boardId, animationId, boardWriterRecord);
        boardRepository.delete(board);
    }

    private BoardEntity getBoardByUser(Long boardId, Long animationId, BoardWriterRecord boardWriterRecord) {
        if (boardWriterRecord.userId() == null) {
            return boardRepository.findByNicknameAndPasswordAndAnimationIdAndId(
                            boardWriterRecord.nickname(),
                            boardWriterRecord.password(),
                            animationId,
                            boardId)
                    .orElseThrow();
        } else {
            return boardRepository.findByUserIdAndAnimationIdAndId(
                            boardWriterRecord.userId(),
                            animationId,
                            boardId)
                    .orElseThrow();
        }
    }

    public BoardLikeRecord likeBoard(Long userId, Long boardId) {
        LikeEntity like = new LikeEntity(userId, boardId);
        UserEntity user = userRepository.findById(userId).orElseThrow();
        BoardEntity board = boardRepository.findById(boardId).orElseThrow();

        like.setUser(user);
        like.setBoard(board);
        likeRepository.save(like);

        boardHotCalculator.likeUpdate(board.getId(), board.getWriteDate(), 1L);

        return BoardLikeRecord.of(board, true);
    }

    public BoardLikeRecord unLikeBoard(Long userId, Long boardId) {
        LikeEntityId likeId = new LikeEntityId(userId, boardId);
        LikeEntity like = likeRepository.findById(likeId).orElseThrow();
        likeRepository.delete(like);

        BoardEntity board = boardRepository.findById(boardId).orElseThrow();
        boardHotCalculator.likeUpdate(board.getId(), board.getWriteDate(), -1L);

        return BoardLikeRecord.of(board, false);
    }

    public PageAnd<BoardListRecord> getRecommendedBoards(Long animationId, Pagination pagination, BoardSearchRecord boardSearchRecord, Long userId) {
        Long recommendedCount = 9L;

        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.by(Sort.Order.desc("writeDate")));
        Page<BoardEntity> boards = switch (boardSearchRecord.kind()) {
            case title ->
                    boardRepository.findAllRecommendedSearchTitle(animationId, pageable, recommendedCount, boardSearchRecord.search());
            case content ->
                    boardRepository.findAllRecommendedSearchContent(animationId, pageable, recommendedCount, boardSearchRecord.search());
            case both ->
                    boardRepository.findAllRecommendedSearchBoth(animationId, pageable, recommendedCount, boardSearchRecord.search());
            case none -> boardRepository.findAllRecommended(animationId, pageable, recommendedCount);
        };

        return new PageAnd<>(
                boards.map(board ->
                        BoardListRecord.of(board, new LikeEntity(userId, board.getId()))
                )
        );
    }

    public List<BoardListRecord> getHotBoards() {
        List<Long> ids = boardHotCalculator.getHotBoards().stream().map(BoardHotDto::getId).toList();
        List<BoardEntity> boards = boardRepository.findAllByIdIn(ids);


        return boards.stream().map(BoardListRecord::of).toList();
    }
}
