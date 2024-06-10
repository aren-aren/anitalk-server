package com.anitalk.app.review;

import com.anitalk.app.review.dto.ReviewRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;

    public List<ReviewRecord> getReviews(Long animationId) {
        return repository.findAllByAnimationId(animationId).stream().map(ReviewRecord::of).toList();
    }

    public ReviewRecord getReviewById(Long id) {
        ReviewEntity reviewEntity = repository.findById(id).orElseThrow();
        return ReviewRecord.of(reviewEntity);
    }

    public ReviewRecord addReview(Long animationId, ReviewRecord review) {
        ReviewEntity entity = review.toEntity();
        entity.setAnimationId(animationId);
        entity.getRate().setReview(entity);

        repository.save(entity);

        return ReviewRecord.of(entity);
    }

    public ReviewRecord putReview(Long id, ReviewRecord review) {
        ReviewEntity entity = repository.findById(id).orElseThrow();
        review.putEntity(entity);
        entity = repository.save(entity);
        return ReviewRecord.of(entity);
    }
}
