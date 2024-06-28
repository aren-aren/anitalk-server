package com.anitalk.app.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findAllByAnimationId(Long animationId, Pageable pagination);

    Page<BoardEntity> findAllByUserId(Long userId, Pageable pageable);

    Optional<BoardEntity> findByUserIdAndAnimationIdAndId(Long userId, Long animationId, Long id);

    Optional<BoardEntity> findByNicknameAndPasswordAndAnimationIdAndId(String nickname, String password, Long animationId, Long boardId);

    Page<BoardEntity> findAllByAnimationIdAndTitleContains(Long animationId, String title, Pageable pageable);

    Page<BoardEntity> findAllByAnimationIdAndContentContains(Long animationId, String content, Pageable pageable);

    Page<BoardEntity> findAllByAnimationIdAndTitleContainsOrContentContains(Long animationId, String title, String content, Pageable pageable);

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

    Page<BoardEntity> findAllByTitleContains(String search, Pageable pageable);

    Page<BoardEntity> findAllByContentContains(String search, Pageable pageable);

    Page<BoardEntity> findAllByTitleContainsOrContentContains(String search, String search1, Pageable pageable);
}
