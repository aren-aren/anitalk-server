package com.anitalk.app.domain.board;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "board")
@NoArgsConstructor
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long animationId;

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

    @Column
    private Boolean isDeleted = false;

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

//    @OneToMany
//    @JoinColumn(name = "board_id")
//    List<LikeEntity> like;

    @Builder
    public BoardEntity(Long animationId,
                       String title,
                       String content,
                       String ip,
                       String nickname,
                       String password,
                       Long userId,
                       String category) {
        this.animationId = animationId;
        this.title = title;
        this.content = content;
        this.ip = ip;
        this.nickname = nickname;
        this.password = password;
        this.userId = userId;
        this.category = BoardCategory.valueOf(category);
    }
}
