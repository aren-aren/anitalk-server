package com.anitalk.app.domain.review;

import com.anitalk.app.domain.review.dto.ReviewRecord;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animations/{animationId}/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewRecord>> getReviews(@PathVariable Long animationId){
        List<ReviewRecord> boardRecords = reviewService.getReviews(animationId);
        return ResponseEntity.ok(boardRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewRecord> getReviewById(@PathVariable Long id){
        ReviewRecord boardRecord = reviewService.getReviewById(id);
        return ResponseEntity.ok(boardRecord);
    }

    @PostMapping
    public ResponseEntity<ReviewRecord> addReview(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @RequestBody ReviewRecord review){
        review = new ReviewRecord(review.id(), animationId, user.id(), review.content(), review.rate());
        ReviewRecord boardRecord = reviewService.addReview(review);
        return ResponseEntity.ok(boardRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewRecord> putReview(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long id,
            @RequestBody ReviewRecord review){
        review = new ReviewRecord(id, null, user.id(), review.content(), review.rate());
        ReviewRecord putBoard = reviewService.putReview(review);
        return ResponseEntity.ok(putBoard);
    }
}
