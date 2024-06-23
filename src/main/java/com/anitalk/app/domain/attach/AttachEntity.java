package com.anitalk.app.domain.attach;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@Table(name = "attach")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachEntity implements Serializable {
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

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;

    @Column(name = "upload_date", length = 20)
    private String uploadDate;

}
