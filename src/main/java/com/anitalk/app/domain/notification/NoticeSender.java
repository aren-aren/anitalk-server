package com.anitalk.app.domain.notification;

import com.anitalk.app.domain.user.UserService;
import com.anitalk.app.domain.user.dto.UserRecord;
import com.anitalk.app.domain.user.dto.UsernameRecord;
import com.anitalk.app.utils.DateManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NoticeSender {
    private final SimpMessagingTemplate template;
    private final UserService userService;

    public void sendNotice(UsernameRecord from, Long to, NoticeType type, NoticeContent target, NoticeContent content){
        try {
            if(from.id() != null) from = getUserRecord(from.id());
            template.convertAndSend("/noti/" + to, new NoticeRecord(from, target, content, type, DateManager.nowDateTime()));
        } catch (Exception e){
            e.printStackTrace();
            log.error("알림 보내기 실패");
        }
    }

    private UsernameRecord getUserRecord(Long id){
        return userService.getUserById(id);
    }
}
