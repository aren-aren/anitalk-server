package com.anitalk.app.domain.animation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteEntityId implements java.io.Serializable {
    private static final long serialVersionUID = 1625546342286189396L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "animation_id", nullable = false)
    private Long animationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FavoriteEntityId entity = (FavoriteEntityId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.animationId, entity.animationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, animationId);
    }

}
