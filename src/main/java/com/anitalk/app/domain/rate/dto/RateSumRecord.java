package com.anitalk.app.domain.rate.dto;

import com.anitalk.app.domain.rate.RateSumEntity;

public record RateSumRecord(
        Long animationId,
        Integer count,
        Integer quality,
        Integer story,
        Integer directing,
        Integer music,
        Integer originality,
        Integer enjoy
) {
    public static RateSumRecord of(RateSumEntity entity){
        return new RateSumRecord(
                entity.getId(),
                entity.getCount(),
                entity.getQuality(),
                entity.getStory(),
                entity.getDirecting(),
                entity.getMusic(),
                entity.getOriginality(),
                entity.getEnjoy()
        );
    }
}
