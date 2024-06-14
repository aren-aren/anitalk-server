package com.anitalk.app.domain.board;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "`like`")
@NoArgsConstructor
public class LikeEntity {
    @EmbeddedId
    private LikeEntityId id;

    LikeEntity(Long userId, Long boardId){
        id = new LikeEntityId(userId, boardId);
    }
}
