package com.anitalk.app.board;

import com.anitalk.app.board.dto.BoardAddRecord;
import com.anitalk.app.board.dto.BoardListRecord;
import com.anitalk.app.board.dto.BoardRecord;
import com.anitalk.app.utils.DateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

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
        return BoardRecord.of(entity);
    }

    public BoardRecord putBoard(Long id, Long animationId, BoardRecord record) {
        BoardEntity entity = boardRepository.findByIdAndAnimationId(id, animationId).orElseThrow();
        record.putEntity(entity);

        entity = boardRepository.save(entity);
        return BoardRecord.of(entity);
    }

}
