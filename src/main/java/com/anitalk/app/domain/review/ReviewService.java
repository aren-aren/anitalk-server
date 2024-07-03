package com.anitalk.app.domain.review;

import com.anitalk.app.domain.rate.RateSumEntity;
import com.anitalk.app.domain.rate.RateSumRepository;
import com.anitalk.app.domain.review.dto.ReviewRecord;
import com.anitalk.app.commons.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;
    private final RateSumRepository rateSumRepository;

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
        entity = repository.save(entity);

        RateSumEntity rateSumEntity = rateSumRepository.findById(review.animationId()).orElse(new RateSumEntity(review.animationId()));
        rateSumEntity.plusRate(entity.getRate());
        rateSumRepository.save(rateSumEntity);

        return ReviewRecord.of(entity);
    }

    public ReviewRecord putReview(ReviewRecord review) {
        ReviewEntity entity = repository.findById(review.id()).orElseThrow();
        review.putEntity(entity);
        entity = repository.save(entity);
        return ReviewRecord.of(entity);
    }

    public void deleteReview(Long reviewId, Long userId) {
        ReviewEntity review = repository.findByIdAndUserId(reviewId, userId).orElseThrow();
        repository.delete(review);
    }
}
