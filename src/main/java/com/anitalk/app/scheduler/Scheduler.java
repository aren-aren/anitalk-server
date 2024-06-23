package com.anitalk.app.scheduler;

import com.anitalk.app.domain.attach.AttachManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {
    public final AttachManager attachManager;

    @Scheduled(cron = "0 30 4 * * ?")
    public void attachDeleteScheduler(){
        try {
            attachManager.deleteNoParentAttach();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
