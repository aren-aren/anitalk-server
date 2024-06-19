package com.anitalk.app.domain.board;

import com.anitalk.app.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "`like`")
@NoArgsConstructor
public class LikeEntity {
    @EmbeddedId
    private LikeEntityId id;

    @ManyToOne
    @MapsId("boardId")
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    LikeEntity(Long userId, Long boardId) {
        id = new LikeEntityId(userId, boardId);
    }
}
