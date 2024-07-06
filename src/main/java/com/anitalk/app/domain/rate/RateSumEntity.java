package com.anitalk.app.domain.rate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rate_sum")
@NoArgsConstructor
public class RateSumEntity {
    @Id
    @Column(name = "animation_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "count", nullable = false)
    private Integer count;

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

    public RateSumEntity(Long animationId){
        this.id = animationId;
        this.count = 0;
        this.quality = 0;
        this.story = 0;
        this.directing = 0;
        this.music = 0;
        this.originality = 0;
        this.enjoy = 0;
    }

    public void plusRate(RateEntity rate) {
        this.count += 1;
        this.quality += rate.getQuality();
        this.story += rate.getStory();
        this.directing += rate.getDirecting();
        this.music += rate.getMusic();
        this.originality += rate.getOriginality();
        this.enjoy += rate.getEnjoy();
    }

    public void minusRate(RateEntity rate) {
        if(count == 0) return;

        this.count -= 1;
        this.quality -= rate.getQuality();
        this.story -= rate.getStory();
        this.directing -= rate.getDirecting();
        this.music -= rate.getMusic();
        this.originality -= rate.getOriginality();
        this.enjoy -= rate.getEnjoy();
    }
}
