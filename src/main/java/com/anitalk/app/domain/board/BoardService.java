package com.anitalk.app.domain.board;

import com.anitalk.app.domain.attach.AttachManager;
import com.anitalk.app.domain.board.dto.BoardAddRecord;
import com.anitalk.app.domain.board.dto.BoardListRecord;
import com.anitalk.app.domain.board.dto.BoardRecord;
import com.anitalk.app.utils.DateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final AttachManager attachManager;

    public List<BoardListRecord> getBoards(Long animationId) {
        return boardRepository.findAllByAnimationId(animationId).stream()
                .map(BoardListRecord::of)
                .toList();
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


        entity = boardRepository.save(entity);

        attachManager.connectAttaches("board", entity.getId(), board.attaches());

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
}
