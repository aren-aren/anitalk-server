package com.anitalk.app.board;

import com.anitalk.app.board.dto.BoardListRecord;
import com.anitalk.app.board.dto.BoardRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/animations/{animationId}/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardListRecord>> getBoards(@PathVariable Long animationId){
        try {
            List<BoardListRecord> boardRecords = boardService.getBoards(animationId);
            return ResponseEntity.ok(boardRecords);
        } catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardRecord> getBoardById(@PathVariable Long animationId, @PathVariable Long id){
        try{
            BoardRecord boardRecord = boardService.getBoardById(animationId, id);
            return ResponseEntity.ok(boardRecord);
        } catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<BoardRecord> addBoard(@PathVariable Long animationId, @RequestBody BoardRecord board){
        try{
            BoardRecord boardRecord = boardService.addBoard(animationId, board);
            return ResponseEntity.ok(boardRecord);
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardRecord> putBoard(@PathVariable Long animationId, @PathVariable Long id, @RequestBody BoardRecord record){
        try{
            BoardRecord putBoard = boardService.putBoard(id, animationId, record);
            return ResponseEntity.ok(putBoard);
        } catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
