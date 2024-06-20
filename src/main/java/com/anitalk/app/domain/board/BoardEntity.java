package com.anitalk.app.domain.board;

import com.anitalk.app.domain.animation.AnimationEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@Table(name = "board")
@NoArgsConstructor
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "animation_id")
    AnimationEntity animation;

    @Column
    private String title;

    @Lob
    @Column
    private String content;

    @Column
    private Long hit;

    @Column
    private String writeDate;

    @Column
    private String modifyDate;

    @Column(name = "is_deleted")
    private Boolean deleted = false;

    @Column
    private String ip;

    @Column
    private String nickname;

    @Column
    private String password;

    @Column
    private Long userId;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private BoardCategory category;

    @OneToMany
    @JoinColumn(name = "board_id")
    Set<LikeEntity> like;

    @Builder
    public BoardEntity(Long animationId,
                       String title,
                       String content,
                       String ip,
                       String nickname,
                       String password,
                       Long userId,
                       String category) {
        this.animation = new AnimationEntity();
        animation.setId(animationId);
        this.title = title;
        this.content = content;
        this.ip = ip;
        this.nickname = nickname;
        this.password = password;
        this.userId = userId;
        this.category = BoardCategory.valueOf(category);
    }
}
