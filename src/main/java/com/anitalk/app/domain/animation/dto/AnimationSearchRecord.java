package com.anitalk.app.domain.animation.dto;

import com.anitalk.app.domain.animation.RankingType;

public record AnimationSearchRecord(
        String search,
        RankingType rankBy
) {
    public RankingType rankBy(){
        if(rankBy == null) return RankingType.ALL;

        return rankBy;
    }
}
