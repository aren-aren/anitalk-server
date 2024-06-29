package com.anitalk.app.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Query(value = """
    select distinct bor
    from BoardEntity bor
        join fetch bor.animation ani
        join fetch bor.user u
    where ani.id = :animationId
    """, countQuery = "select count(distinct bor.id) from BoardEntity bor")
    Page<BoardEntity> findAllByAnimationIdFetchJoin(Long animationId, Pageable pagination);

    Page<BoardEntity> findAllByUserId(Long userId, Pageable pageable);

    Optional<BoardEntity> findByUserIdAndAnimationIdAndId(Long userId, Long animationId, Long id);

    Optional<BoardEntity> findByNicknameAndPasswordAndAnimationIdAndId(String nickname, String password, Long animationId, Long boardId);

    @Query(value = """
    select distinct bor
    from BoardEntity bor
        join fetch bor.animation ani
                join fetch bor.user
    where ani.id = :animationId
        and bor.title like concat('%',:title,'%')
    """, countQuery = "select count(distinct bor.id) from BoardEntity bor")
    Page<BoardEntity> findAllByAnimationIdAndTitleContainsFetchJoin(Long animationId, String title, Pageable pageable);

    @Query(value = """
            select distinct bor
            from BoardEntity bor
                join fetch bor.animation ani
                join fetch bor.user
            where ani.id = :animationId
                and bor.content like concat('%',:content,'%')
            """, countQuery = "select count(distinct bor.id) from BoardEntity bor")
    Page<BoardEntity> findAllByAnimationIdAndContentContainsFetchJoin(Long animationId, String content, Pageable pageable);

    @Query(value = """
    select distinct bor
    from BoardEntity bor
        join fetch bor.animation ani
        join fetch bor.user
    where ani.id = :animationId
        and (bor.content like concat('%',:search,'%') or bor.title like concat('%',:search,'%'))
    """, countQuery = "select count(distinct bor.id) from BoardEntity bor")
    Page<BoardEntity> findAllByAnimationIdAndBothContainsFetchJoin(Long animationId, String search, Pageable pageable);

    @Query("""
        select bor
        from BoardEntity bor
            join LikeEntity like on bor.id = like.board.id
        where bor.animation.id = :animationId
        group by bor.id
        having count(like.id) > :recommendedCount
    """)
    Page<BoardEntity> findAllRecommended(Long animationId, Pageable pageable, Long recommendedCount);

    @Query("""
        select bor
        from BoardEntity bor
            join LikeEntity like on bor.id = like.board.id
        where bor.animation.id = :animationId
            and bor.title like concat('%', :search , '%')
        group by bor.id
        having count(like.id) > :recommendedCount
    """)
    Page<BoardEntity> findAllRecommendedSearchTitle(Long animationId, Pageable pageable, Long recommendedCount, String search);

    @Query("""
        select bor
        from BoardEntity bor
            join LikeEntity like on bor.id = like.board.id
        where bor.animation.id = :animationId
            and bor.content like concat('%', :search , '%')
        group by bor.id
        having count(like.id) > :recommendedCount
    """)
    Page<BoardEntity> findAllRecommendedSearchContent(Long animationId, Pageable pageable, Long recommendedCount, String search);

    @Query("""
        select bor
        from BoardEntity bor
            join LikeEntity like on bor.id = like.board.id
        where bor.animation.id = :animationId
            and (bor.title like concat('%', :search , '%') or bor.content like concat('%', :search , '%'))
        group by bor.id
        having count(like.id) > :recommendedCount
    """)
    Page<BoardEntity> findAllRecommendedSearchBoth(Long animationId, Pageable pageable, Long recommendedCount, String search);

    @Query(value = """
    select distinct bor
    from BoardEntity bor
        join fetch bor.animation ani
        join fetch bor.user
    where bor.title like concat('%',:search,'%')
    """, countQuery = "select count(distinct bor.id) from BoardEntity bor")
    Page<BoardEntity> findAllByTitleContainsFetchJoin(String search, Pageable pageable);

    @Query(value = """
    select distinct bor
    from BoardEntity bor
        join fetch bor.animation ani
        join fetch bor.user
    where bor.content like concat('%',:search,'%')
    """, countQuery = "select count(distinct bor.id) from BoardEntity bor")
    Page<BoardEntity> findAllByContentContainsFetchJoin(String search, Pageable pageable);

    @Query(value = """
    select distinct bor
    from BoardEntity bor
        join fetch bor.animation ani
        join fetch bor.user
    where (bor.content like concat('%',:search,'%') or bor.title like concat('%',:search,'%'))
    """, countQuery = "select count(distinct bor.id) from BoardEntity bor")
    Page<BoardEntity> findAllByBothContainsFetchJoin(String search, Pageable pageable);

    @Query(value = """
    select distinct bor
    from BoardEntity bor
        join fetch bor.animation ani
        join fetch bor.user
    """, countQuery = "select count(distinct bor.id) from BoardEntity bor")
    Page<BoardEntity> findAllFetchJoin(Pageable pageable);
}
