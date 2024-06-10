package com.anitalk.app.review;

import com.anitalk.app.review.dto.ReviewRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animations/{animationId}/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewRecord>> getBoards(@PathVariable Long animationId){
        List<ReviewRecord> boardRecords = reviewService.getReviews(animationId);
        return ResponseEntity.ok(boardRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewRecord> getBoardById(@PathVariable Long id){
        ReviewRecord boardRecord = reviewService.getReviewById(id);
        return ResponseEntity.ok(boardRecord);
    }

    @PostMapping
    public ResponseEntity<ReviewRecord> addBoard(@PathVariable Long animationId, @RequestBody ReviewRecord review){
        System.out.println(review);
        ReviewRecord boardRecord = reviewService.addReview(animationId, review);
        return ResponseEntity.ok(boardRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewRecord> putBoard(@PathVariable Long id, @RequestBody ReviewRecord review){
        ReviewRecord putBoard = reviewService.putReview(id, review);
        return ResponseEntity.ok(putBoard);
    }
}
