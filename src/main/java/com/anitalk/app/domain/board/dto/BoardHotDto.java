package com.anitalk.app.domain.board.dto;

import com.anitalk.app.utils.DateManager;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardHotDto {
    private Long id;
    private Long likeCount;
    private String writeDate;
    private Long lastLikeMilliseconds;
    private Double score;

    public void setScore(){
        Long diffWriteDate = DateManager.getDiffToday(writeDate);

        score = (likeCount.doubleValue()*10/diffWriteDate.doubleValue());
    }

    public void updateLikeCount(Long dLikeCount) {
        if(DateManager.getNowMilliseconds() - lastLikeMilliseconds > 1000*60*10){
            likeCount = 0L;
        }
        likeCount = Math.max(dLikeCount + likeCount, 0);
    }
}
