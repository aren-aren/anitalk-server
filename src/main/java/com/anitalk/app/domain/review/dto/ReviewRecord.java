package com.anitalk.app.domain.review.dto;

import com.anitalk.app.domain.rate.RateEntity;
import com.anitalk.app.domain.rate.dto.RateRecord;
import com.anitalk.app.domain.review.ReviewEntity;

public record ReviewRecord(
        Long id,
        Long animationId,
        Long userId,
        String content,
        RateRecord rate
) {
    public static ReviewRecord of(ReviewEntity entity){
        return new ReviewRecord(
                entity.getId(),
                entity.getAnimationId(),
                entity.getUserId(),
                entity.getContent(),
                RateRecord.of(entity.getRate())
        );
    }

    public ReviewEntity toEntity(){
        return ReviewEntity.builder()
                .id(id())
                .animationId(animationId())
                .userId(userId())
                .content(content())
                .rate(rate().toEntity())
                .build();
    }

    public void putEntity(ReviewEntity entity) {
        if(content != null) entity.setContent(content());

        if(rate != null) {
            RateEntity rateEntity = rate().toEntity();
            rateEntity.setId(entity.getRate().getId());
            rateEntity.setReview(entity);
            entity.setRate(rateEntity);
        }
    }
}
