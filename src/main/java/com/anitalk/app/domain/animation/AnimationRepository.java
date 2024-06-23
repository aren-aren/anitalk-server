package com.anitalk.app.domain.animation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimationRepository extends JpaRepository<AnimationEntity, Long> {
    AnimationEntity findByName(String name);

    @Query("""
        select distinct ani
        from AnimationEntity ani
            join fetch FavoriteEntity fav on ani.id = fav.animation.id
        where fav.user.id = :userId
        order by ani.currentDate desc
    """)
    Page<AnimationEntity> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
        select distinct ani
        from AnimationEntity ani
            left join fetch BoardEntity bor on bor.animation.id = ani.id and :startDate <= bor.writeDate
        group by ani.id
        order by count(bor.id) desc, ani.currentDate desc
    """)
    Page<AnimationEntity> findAllHotRanking(Pageable pageable, String startDate);

    @Query("""
        select distinct ani
        from AnimationEntity ani
            left join ReviewEntity rev on rev.animationId = ani.id
        group by ani.id
        order by sum(rev.sumRate) desc, ani.currentDate desc
    """)
    Page<AnimationEntity> findAllRateRanking(Pageable pageable);
}
