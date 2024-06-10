package com.anitalk.app.review;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "review")
@NoArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "animation_id", nullable = false)
    private Long animationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private RateEntity rate;

    @Builder
    public ReviewEntity(Long id, Long animationId, Long userId, String content, RateEntity rate) {
        this.id = id;
        this.animationId = animationId;
        this.userId = userId;
        this.content = content;
        this.rate = rate;
    }
}
