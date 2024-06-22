package com.anitalk.app.domain.chatting;

import com.anitalk.app.domain.chatting.dto.MessageRecord;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChattingService {
    private final Map<Long, LinkedList<MessageRecord>> repository;
    private final Integer maxSize;

    ChattingService(){
        repository = new ConcurrentHashMap<>();
        maxSize = 10;
    }

    public List<MessageRecord> getMessagesByAnimationId(Long animationId) {
        return repository.getOrDefault(animationId, new LinkedList<>());
    }

    public void setMessage(Long animationId, MessageRecord message) {
        repository.putIfAbsent(animationId, new LinkedList<>());

        Queue<MessageRecord> messageRecords = repository.get(animationId);
        synchronized (messageRecords){
            if(messageRecords.size() >= maxSize){
                messageRecords.poll();
            }

            messageRecords.add(message);
        }
    }
}
