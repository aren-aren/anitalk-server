package com.anitalk.app.animation.dto;

import com.anitalk.app.animation.AnimationEntity;
import com.anitalk.app.animation.Day;

public record AnimationRecord(
        Long id,
        String name,
        String plot,
        String startDate,
        String endDate,
        Integer episodes,
        String day
) {
    public AnimationRecord{
        Day.valueOf(day);
    }

    public static AnimationRecord of(AnimationEntity entity){
        return new AnimationRecord(
                entity.getId(),
                entity.getName(),
                entity.getPlot(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getEpisodes(),
                entity.getDay().toString()
        );
    }

    public AnimationEntity toEntity() {
        return AnimationEntity.builder()
                .name(name())
                .plot(plot())
                .startDate(startDate())
                .endDate(endDate())
                .episodes(episodes())
                .day(Day.valueOf(day()))
                .build();
    }
}
