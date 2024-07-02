package com.anitalk.app.domain.search;

import com.anitalk.app.utils.DateManager;
import lombok.Data;

@Data
public class SearchWordDto {
    private String word;
    private Long lastSearchMilliseconds;
    private Long count;
    private Double score;

    public SearchWordDto(String word) {
        this.word = word;
        this.lastSearchMilliseconds = null;
        this.count = 0L;
        this.score = 0.0;
    }

    public void setScore(){
        Long nowMilliseconds = DateManager.getNowMilliseconds();
        Long timeDiff = nowMilliseconds - lastSearchMilliseconds;

        if(timeDiff > 1000*60*10){
            count /= 2;
        }

        score = count.doubleValue()*10/timeDiff.doubleValue();
    }

    public void upCount() {
        this.count++;
    }
}
