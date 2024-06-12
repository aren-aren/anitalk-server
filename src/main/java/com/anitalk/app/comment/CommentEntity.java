package com.anitalk.app.comment;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "comment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "write_date", nullable = false, length = 19)
    private String writeDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "nickname", length = 10)
    private String nickname;

    @Column(name = "password", length = 10)
    private String password;

    @Column(name = "ip", length = 15)
    private String ip;

    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "depth")
    private Long depth;

    @Column(name = "step")
    private Long step;
}
