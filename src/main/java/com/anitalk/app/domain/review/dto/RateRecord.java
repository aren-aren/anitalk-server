package com.anitalk.app.domain.review.dto;

import com.anitalk.app.domain.review.RateEntity;

public record RateRecord(
        Long reviewId,
        Integer quality,
        Integer story,
        Integer directing,
        Integer music,
        Integer originality,
        Integer enjoy
) {
    public static RateRecord of(RateEntity entity){
        return new RateRecord(
                entity.getId(),
                entity.getQuality(),
                entity.getStory(),
                entity.getDirecting(),
                entity.getMusic(),
                entity.getOriginality(),
                entity.getEnjoy()
        );
    }

    public RateEntity toEntity(){
        return RateEntity.builder()
                .id(reviewId())
                .quality(quality())
                .story(story())
                .directing(directing())
                .music(music())
                .originality(originality())
                .enjoy(enjoy())
                .build();
    }
}
