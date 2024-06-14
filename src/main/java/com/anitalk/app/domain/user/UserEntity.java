package com.anitalk.app.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "password", nullable = false, length = 1000)
    private String password;

    @Builder
    public UserEntity(Long id, String email, String nickname, String password) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
