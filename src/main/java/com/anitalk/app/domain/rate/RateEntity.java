package com.anitalk.app.domain.rate;

import com.anitalk.app.domain.review.ReviewEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "rate")
@NoArgsConstructor
public class RateEntity {
    @Id
    @Column(name = "review_id")
    private Long id;

    @Column(name = "quality")
    private Integer quality;

    @Column(name = "story")
    private Integer story;

    @Column(name = "directing")
    private Integer directing;

    @Column(name = "music")
    private Integer music;

    @Column(name = "originality")
    private Integer originality;

    @Column(name = "enjoy")
    private Integer enjoy;

    @OneToOne
    @MapsId
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

    @Builder
    public RateEntity(Long id, Integer quality, Integer story, Integer directing, Integer music, Integer originality, Integer enjoy) {
        this.id = id;
        this.quality = quality;
        this.story = story;
        this.directing = directing;
        this.music = music;
        this.originality = originality;
        this.enjoy = enjoy;
    }
}
