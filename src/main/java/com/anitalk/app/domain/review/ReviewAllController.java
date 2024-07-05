package com.anitalk.app.domain.review;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.commons.Pagination;
import com.anitalk.app.domain.review.dto.ReviewRecord;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewAllController {

    private final ReviewService reviewService;

    @GetMapping("/users")
    public ResponseEntity<PageAnd<ReviewRecord>> getReviewsByUserId(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            Pagination pagination
    ) throws Exception {
        if(user == null) throw new Exception("로그인이 필요합니다.");

        PageAnd<ReviewRecord> reviews = reviewService.getReviewsByUserId(user.id(), pagination);
        return ResponseEntity.ok(reviews);
    }
}
