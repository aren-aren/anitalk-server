package com.anitalk.app.domain.animation.dto;

public record FavoriteRecord(
        Long userId,
        Integer count,
        boolean isFavorite
) {

}
