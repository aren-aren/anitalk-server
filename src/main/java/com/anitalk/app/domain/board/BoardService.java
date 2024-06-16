package com.anitalk.app.domain.board;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.attach.AttachManager;
import com.anitalk.app.domain.board.dto.BoardAddRecord;
import com.anitalk.app.domain.board.dto.BoardListRecord;
import com.anitalk.app.domain.board.dto.BoardRecord;
import com.anitalk.app.utils.DateManager;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final AttachManager attachManager;

    public Page<BoardListRecord> getBoards(Long animationId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
        return boardRepository.findAllByAnimationId(animationId, pageable).map(BoardListRecord::of);
    }

    public BoardRecord getBoardById(Long animationId, Long id) {
        BoardEntity entity = boardRepository.findByIdAndAnimationId(id, animationId).orElseThrow();
        return BoardRecord.of(entity);
    }

    public BoardRecord addBoard(Long animationId, BoardAddRecord board) {
        BoardEntity entity = board.toEntity();
        entity.setAnimationId(animationId);
        entity.setHit(0L);
        entity.setIsDeleted(false);
        entity.setWriteDate(DateManager.nowDateTime());

        /* ip 처리 필요 */
        entity.setIp("123.123.123.123");


        entity = boardRepository.save(entity);

        if(board.attaches() != null){
            attachManager.connectAttaches("board", entity.getId(), board.attaches());
        }

        return BoardRecord.of(entity);
    }

    public BoardRecord putBoard(Long id, Long animationId, BoardAddRecord record) {
        BoardEntity entity = boardRepository.findByIdAndAnimationId(id, animationId).orElseThrow();
        record.putEntity(entity);

        entity = boardRepository.save(entity);
        return BoardRecord.of(entity);
    }

    public void likeBoard(Long userId, Long boardId) {
        LikeEntity like = new LikeEntity(userId, boardId);
        likeRepository.save(like);
    }

    public void unLikeBoard(Long userId, Long boardId) {
        LikeEntityId likeId = new LikeEntityId(userId, boardId);
        LikeEntity like = likeRepository.findById(likeId).orElseThrow();
        likeRepository.delete(like);
    }

    public PageAnd<BoardListRecord> getBoardsByUserId(Long userId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
        Page<BoardEntity> boardEntities = boardRepository.findAllByUserId(userId, pageable);

        return new PageAnd<>(boardEntities.map(BoardListRecord::of));
    }
}
