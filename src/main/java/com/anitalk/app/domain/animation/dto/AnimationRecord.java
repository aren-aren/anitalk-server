package com.anitalk.app.domain.animation.dto;

import com.anitalk.app.domain.animation.AnimationEntity;
import com.anitalk.app.domain.animation.FavoriteEntity;
import com.anitalk.app.domain.rate.dto.RateSumRecord;

public record AnimationRecord(
        Long id,
        String name,
        String plot,
        String condition,
        String productCompany,
        String producer,
        String writer,
        String originWriter,
        String season,
        String onDate,
        Integer episode,
        String startDate,
        String currentDate,
        boolean isReview,
        String thumbnailUrl,
        FavoriteRecord favorite,
        RateSumRecord rate
) {

    public static AnimationRecord of(AnimationEntity entity, String url, Long userId){
        return AnimationRecord.of(entity, url, userId, false);
    }

    public static AnimationRecord of(AnimationEntity entity, String url, Long userId, boolean isReview){
        boolean isFavorite = false;
        FavoriteRecord favoriteRecord = null;

        if(entity.getFavorites() != null){
            if(userId != null){
                isFavorite = entity.getFavorites().contains(new FavoriteEntity(userId, entity.getId()));
            }

            favoriteRecord = new FavoriteRecord(
                    userId,
                    entity.getFavorites().size(),
                    isFavorite
            );
        }

        return new AnimationRecord(
                entity.getId(),
                entity.getName(),
                entity.getPlot(),
                entity.getCondition(),
                entity.getProductCompany(),
                entity.getProducer(),
                entity.getWriter(),
                entity.getOriginWriter(),
                entity.getSeason(),
                entity.getOnDate(),
                entity.getEpisode(),
                entity.getStartDate(),
                entity.getCurrentDate(),
                isReview,
                url,
                favoriteRecord,
                RateSumRecord.of(entity.getRateSum())
        );
    }
}
