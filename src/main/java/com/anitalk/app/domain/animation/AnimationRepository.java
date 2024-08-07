package com.anitalk.app.domain.animation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimationRepository extends JpaRepository<AnimationEntity, Long> {
    AnimationEntity findByName(String name);

    @Query("""
        select distinct ani
        from AnimationEntity ani
            join fetch ani.favorites fav
            join fetch ani.rateSum rate
        where fav.user.id = :userId
        order by ani.currentDate desc
    """)
    Page<AnimationEntity> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
        select distinct ani
        from AnimationEntity ani
            join fetch ani.rateSum rate
            left join BoardEntity bor on bor.animation.id = ani.id and :startDate <= bor.writeDate
        group by ani.id
        order by count(bor.id) desc, ani.currentDate desc
    """)
    Page<AnimationEntity> findAllHotRanking(Pageable pageable, String startDate);

    @Query("""
        select distinct ani
        from AnimationEntity ani
            left join fetch ani.rateSum rate
        order by rate.directing + rate.enjoy + rate.music + rate.originality + rate.quality + rate.story desc, ani.currentDate desc
    """)
    Page<AnimationEntity> findAllRateRanking(Pageable pageable);

    Page<AnimationEntity> findAllByNameContains(String search, Pageable pageable);

    @Query("""
        select distinct ani
        from AnimationEntity ani
            left join fetch ani.rateSum rate
    """)
    Page<AnimationEntity> findAllFetchJoin(Pageable pageable);
}
