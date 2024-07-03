package com.anitalk.app.domain.review;

import com.anitalk.app.domain.rate.RateEntity;
import com.anitalk.app.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "review")
@NoArgsConstructor
@SQLRestriction("is_deleted=false")
@SQLDelete(sql="update review set is_deleted = true where id = ?")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "animation_id", nullable = false)
    private Long animationId;

    @ManyToOne
    private UserEntity user;

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
        this.user = UserEntity.builder().id(userId).build();
        this.content = content;
        this.rate = rate;
    }
}
