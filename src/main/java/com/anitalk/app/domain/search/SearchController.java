package com.anitalk.app.domain.search;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final RealtimeSearchManager searchManager;

    @GetMapping("/api/searches")
    public ResponseEntity<List<SearchWordDto>> getSearch(){
        return ResponseEntity.ok(searchManager.getSearchRankings());
    }
}
