package com.anitalk.app.domain.review;

import com.anitalk.app.domain.review.dto.ReviewRecord;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;

    public Page<ReviewRecord> getReviews(Long animationId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
        return repository.findAllByAnimationId(animationId, pageable).map(ReviewRecord::of);
    }

    public ReviewRecord getReviewById(Long id) {
        ReviewEntity reviewEntity = repository.findById(id).orElseThrow();
        return ReviewRecord.of(reviewEntity);
    }

    public ReviewRecord addReview(ReviewRecord review) {
        ReviewEntity entity = review.toEntity();
        entity.getRate().setReview(entity);

        repository.save(entity);

        return ReviewRecord.of(entity);
    }

    public ReviewRecord putReview(ReviewRecord review) {
        ReviewEntity entity = repository.findById(review.id()).orElseThrow();
        review.putEntity(entity);
        entity = repository.save(entity);
        return ReviewRecord.of(entity);
    }
}
