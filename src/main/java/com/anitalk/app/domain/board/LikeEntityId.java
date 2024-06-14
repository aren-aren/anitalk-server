package com.anitalk.app.domain.board;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LikeEntityId implements java.io.Serializable {
    private static final long serialVersionUID = 947944292274751143L;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;
}
