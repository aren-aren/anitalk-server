package com.anitalk.app.domain.animation.dto;

public record AnimationSearchRecord(
        String search,
        RankingType rankBy
) {
    public RankingType rankBy(){
        if(rankBy == null) return RankingType.ALL;

        return rankBy;
    }

    public enum RankingType {
        HOT, RATE, ALL
    }
}
