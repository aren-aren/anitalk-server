package com.anitalk.app.domain.animation.dto;

import com.anitalk.app.domain.animation.AnimationEntity;

import java.util.Set;

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
        Set<String> attach,
        String thumbnailUrl
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
                null,
                url
        );
    }

    public AnimationEntity toEntity() {
        return AnimationEntity.builder()
                .id(id())
                .name(name())
                .plot(plot())
                .condition(condition())
                .startDate(startDate())
                .episode(episode())
                .onDate(onDate())
                .season(season())
                .currentDate(currentDate())
                .writer(writer())
                .originWriter(originWriter())
                .producer(producer())
                .productCompany(productCompany())
                .build();
    }

    public void putEntity(AnimationEntity entity) {
        if(name() != null){
            entity.setName(name());
        }
        if(plot() != null){
            entity.setPlot(plot());
        }
        if(startDate() != null){
            entity.setStartDate(startDate());
        }
        if(episode() != null){
            entity.setEpisode(episode());
        }
        if(productCompany() != null){
            entity.setProductCompany(productCompany());
        }
        if(producer() != null){
            entity.setProducer(producer());
        }
        if(writer() != null){
            entity.setWriter(writer());
        }
        if(originWriter() != null){
            entity.setOriginWriter(originWriter());
        }
        if(season() != null){
            entity.setSeason(season());
        }
        if(onDate() != null){
            entity.setOnDate(onDate());
        }
        if(currentDate() != null){
            entity.setCurrentDate(currentDate());
        }
    }
}
