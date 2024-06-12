package com.anitalk.app.attach;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "attach")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "origin_name", nullable = false, length = 50)
    private String originName;

}
