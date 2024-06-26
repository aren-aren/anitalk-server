package com.anitalk.app.domain.animation;

import com.anitalk.app.domain.board.BoardEntity;
import com.anitalk.app.domain.rate.RateSumEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name = "animation")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Lob
    @Column(name = "plot")
    private String plot;

    @Column(name = "`condition`", length = 10)
    private String condition;

    @Column(name = "product_company", length = 30)
    private String productCompany;

    @Column(name = "producer", length = 30)
    private String producer;

    @Column(name = "writer", length = 30)
    private String writer;

    @Column(name = "origin_writer", length = 30)
    private String originWriter;

    @Column(name = "season", length = 10)
    private String season;

    @Column(name = "on_date", length = 14)
    private String onDate;

    @Column(name = "episode")
    private Integer episode;

    @Column(name = "start_date", length = 10)
    private String startDate;

    @Column(name = "`current_date`", length = 10)
    private String currentDate;

    @OneToMany(mappedBy = "animation")
    private List<BoardEntity> boards;

    @OneToMany(mappedBy = "animation", fetch = FetchType.EAGER)
    @BatchSize(size = 100)
    private Set<FavoriteEntity> favorites;

    @OneToOne
    @PrimaryKeyJoinColumn
    private RateSumEntity rateSum;
}
