package com.anitalk.app.domain.chatting;

import com.anitalk.app.domain.chatting.dto.MessageRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChattingController {
    private final ChattingService chattingService;

    @MessageMapping("/{animationId}")
    @SendTo("/ani/{animationId}")
    public MessageRecord sendChatting(@DestinationVariable Long animationId, MessageRecord message){
        chattingService.setMessage(animationId, message);
        return message;
    }

    @GetMapping("/api/animations/{animationId}/chattings")
    public List<MessageRecord> getMessages(@PathVariable Long animationId){
        return chattingService.getMessagesByAnimationId(animationId);
    }
}
