package com.anitalk.app.scheduler;

import com.anitalk.app.domain.attach.AttachManager;
import com.anitalk.app.domain.board.BoardHotCalculator;
import com.anitalk.app.domain.search.RealtimeSearchManager;
import com.anitalk.app.utils.DateManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    public final AttachManager attachManager;
    private final BoardHotCalculator boardHotCalculator;
    private final RealtimeSearchManager realtimeSearchManager;

    @Scheduled(cron = "0 30 4 * * ?")
    public void attachDeleteScheduler(){
        try {
            attachManager.deleteNoParentAttach();
            //log.info("noParentAttachScheduler run");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void calcHotBoards(){
        try {
            log.info("----");
            Long start = DateManager.getNowMilliseconds();
            boardHotCalculator.calcHotBoards();
            log.info("boardHotCalculatorScheduler run - time : {}ms", DateManager.getNowMilliseconds() - start);

            start = DateManager.getNowMilliseconds();
            realtimeSearchManager.calcScore();
            log.info("realtimeSearchManagerScheduler run - time : {}ms", DateManager.getNowMilliseconds() - start);
            log.info("----");
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
