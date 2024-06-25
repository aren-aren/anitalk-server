package com.anitalk.app.domain.comment;

import com.anitalk.app.domain.board.BoardEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "comment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted=false")
@SQLDelete(sql="update comment set is_deleted = true where id = ?")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "board_id")
    BoardEntity board;

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
