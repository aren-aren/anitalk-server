package com.anitalk.app.domain.animation;

import com.anitalk.app.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "favorite")
@NoArgsConstructor
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

    public FavoriteEntity(Long userId, Long animationId) {
        this.id = new FavoriteEntityId(userId, animationId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
