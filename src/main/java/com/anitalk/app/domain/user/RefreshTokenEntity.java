package com.anitalk.app.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
public class RefreshTokenEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "refresh_token", nullable = false, length = 200)
    private String refreshToken;

    public RefreshTokenEntity(Long id) {
        this.id = id;
    }
}
