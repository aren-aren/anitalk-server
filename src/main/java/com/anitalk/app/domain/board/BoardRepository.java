package com.anitalk.app.domain.board;

import com.anitalk.app.utils.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findAllByAnimationId(Long animationId, Pageable pagination);

    Optional<BoardEntity> findByAnimationId(Long animationId);

    Page<BoardEntity> findAllByUserId(Long userId, Pageable pageable);

    Optional<BoardEntity> findByUserIdAndAnimationIdAndId(Long userId, Long animationId, Long id);

    Optional<BoardEntity> findByNicknameAndPasswordAndAnimationIdAndId(String nickname, String password, Long animationId, Long boardId);

    Optional<BoardEntity> findByIdAndAnimationId(Long id, Long animationId);

    @Query("""
        select distinct bor
        from BoardEntity bor
            join fetch LikeEntity like on bor.id = like.board.id
        where bor.animation.id = :animationId
        group by bor.id
        having count(like.id) > :recommendedCount
    """)
    Page<BoardEntity> findAllRecommended(Long animationId, Pageable pageable, Long recommendedCount);
}
