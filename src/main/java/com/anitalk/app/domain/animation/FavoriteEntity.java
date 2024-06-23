package com.anitalk.app.domain.animation;

import com.anitalk.app.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorite")
public class FavoriteEntity {
    @EmbeddedId
    private FavoriteEntityId id;

    @ManyToOne
    @MapsId("animationId")
    @JoinColumn(name = "animation_id")
    private AnimationEntity animation;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
