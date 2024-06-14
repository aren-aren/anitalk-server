package com.anitalk.app.domain.attach;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "attach")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "category", length = 20)
    private String category;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "origin_name", nullable = false, length = 50)
    private String originName;

    @Column(name = "parent_id")
    private Long parentId;
}
