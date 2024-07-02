package com.anitalk.app.domain.search;

import com.anitalk.app.utils.DateManager;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class RealtimeSearchManager {
    private final Map<String, SearchWordDto> searchWords;
    private final Deque<SearchWordDto> rankWords;

    public RealtimeSearchManager() {
        searchWords = new ConcurrentHashMap<>();
        rankWords = new ConcurrentLinkedDeque<>();
    }

    public void saveSearch(String word){
        searchWords.putIfAbsent(word, new SearchWordDto(word));

        SearchWordDto searchWordDto = searchWords.get(word);
        searchWordDto.setLastSearchMilliseconds(DateManager.getNowMilliseconds());
        searchWordDto.upCount();
    }

    public void calcScore(){
        Queue<SearchWordDto> heap = new PriorityQueue<>(Comparator.comparing(SearchWordDto::getScore).reversed());

        searchWords.forEach((key, search) -> {
            search.setScore();
            heap.add(search);
        });

        synchronized (rankWords){
            rankWords.clear();
            SearchWordDto out;

            while(!heap.isEmpty()){
                out = heap.poll();
                if(rankWords.size() < 11){
                    rankWords.add(out);
                    continue;
                }

                if(out.getScore() == 0){
                    searchWords.remove(out.getWord());
                }
            }
        }
    }

    public List<SearchWordDto> getSearchRankings(){
        return this.rankWords.stream().toList();
    }
}
