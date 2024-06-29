package com.anitalk.app.domain.board;

import com.anitalk.app.domain.board.dto.BoardHotDto;
import com.anitalk.app.utils.DateManager;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class BoardHotCalculator {
    private final Deque<BoardHotDto> hotBoards;
    private final Map<Long, BoardHotDto> boards;

    public BoardHotCalculator(){
        hotBoards = new ConcurrentLinkedDeque<>();
        boards = new ConcurrentHashMap<>();
    }

    public void likeUpdate(Long id, String writeDate, Long dLikeCount){
        boards.putIfAbsent(id, new BoardHotDto(id, 0L, writeDate , DateManager.getNowMilliseconds(), 0.0));

        BoardHotDto dto = boards.get(id);
        synchronized (dto){
            dto.updateLikeCount(dLikeCount);
            dto.setLastLikeMilliseconds(DateManager.getNowMilliseconds());
        }
    }

    public void calcHotBoards(){
        Queue<BoardHotDto> heap = new PriorityQueue<>(Comparator.comparing(BoardHotDto::getScore).reversed());

        boards.forEach((key, board) -> {
            board.setScore();
            heap.add(board);
        });

        hotBoards.clear();
        for (int i = 0; i < 10; i++) {
            if(heap.isEmpty()) return;

            hotBoards.add(heap.poll());
        }
    }

    public List<BoardHotDto> getHotBoards(){
        return hotBoards.stream().toList();
    }
}
