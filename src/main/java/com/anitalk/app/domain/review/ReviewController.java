package com.anitalk.app.domain.review;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.commons.StringResult;
import com.anitalk.app.domain.review.dto.ReviewRecord;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.commons.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animations/{animationId}/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<PageAnd<ReviewRecord>> getReviews(@PathVariable Long animationId, Pagination pagination) {
        PageAnd<ReviewRecord> reviewRecord = new PageAnd<>(reviewService.getReviews(animationId, pagination));
        return ResponseEntity.ok(reviewRecord);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewRecord> getReviewById(@PathVariable Long id) {
        ReviewRecord reviewRecord = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewRecord);
    }

    @GetMapping("/my")
    public ResponseEntity<ReviewRecord> getReviewByUserId(
            @PathVariable Long animationId,
            @AuthenticationPrincipal AuthenticateUserRecord user ) {
        ReviewRecord reviewRecord = reviewService.getReviewByUserId(animationId, user);

        return ResponseEntity.ok(reviewRecord);
    }

    @PostMapping
    public ResponseEntity<ReviewRecord> addReview(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId,
            @RequestBody ReviewRecord review) throws Exception {
        if(user == null) throw new Exception("로그인이 필요합니다.");

        review = new ReviewRecord(review.id(), animationId, user.id(), null, review.content(), review.rate());
        ReviewRecord reviewRecord = reviewService.addReview(review);
        return ResponseEntity.ok(reviewRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewRecord> putReview(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long id,
            @RequestBody ReviewRecord review) {
        review = new ReviewRecord(id, null, user.id(), null, review.content(), review.rate());
        ReviewRecord putReview = reviewService.putReview(review);
        return ResponseEntity.ok(putReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StringResult> deleteReview(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long id ) throws Exception {
        if(user == null) throw new Exception("로그인이 필요합니다.");

        reviewService.deleteReview(id, user.id());
        return ResponseEntity.ok(new StringResult("success"));
    }
}
