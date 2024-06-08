package com.anitalk.app.animation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "animation")
public class AnimationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String plot;

    @Column
    private String startDate;

    @Column
    private String endDate;

    @Column
    private Integer episodes;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Day day;
}
