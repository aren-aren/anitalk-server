package com.anitalk.app.domain.animation.dto;

import com.anitalk.app.domain.animation.AnimationEntity;
import com.anitalk.app.domain.animation.FavoriteEntity;

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
        String thumbnailUrl,
        FavoriteRecord favorite
) {

    public static AnimationRecord of(AnimationEntity entity, String url){

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
                url,
                null
        );
    }

    public static AnimationRecord of(AnimationEntity entity, String url, Long userId){

        FavoriteRecord favoriteRecord;
        if(userId != null){
            favoriteRecord = new FavoriteRecord(
                    userId,
                    entity.getFavorites().size(),
                    entity.getFavorites().contains(new FavoriteEntity(userId, entity.getId()))
            );
        } else {
            favoriteRecord = new FavoriteRecord(
                    null,
                    entity.getFavorites().size(),
                    false
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
                url,
                favoriteRecord
        );
    }
}
