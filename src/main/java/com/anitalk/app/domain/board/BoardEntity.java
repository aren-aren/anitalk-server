package com.anitalk.app.domain.board;

import com.anitalk.app.domain.animation.AnimationEntity;
import com.anitalk.app.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "board")
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted=false")
@SQLDelete(sql="update board set is_deleted = true where id = ?")
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

    @ManyToOne
    UserEntity user;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private BoardCategory category;

    @OneToMany
    @JoinColumn(name = "board_id")
    Set<LikeEntity> like;

    public Set<LikeEntity> getLike() {
        if(like == null) return new HashSet<>();

        return like;
    }

    public UserEntity getUser(){
        if(user == null) return UserEntity.builder().build();

        return user;
    }

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
        this.user = UserEntity.builder().id(userId).build();
        this.category = BoardCategory.valueOf(category);
    }
}
