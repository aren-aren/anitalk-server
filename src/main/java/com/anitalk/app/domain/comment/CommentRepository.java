package com.anitalk.app.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByBoardIdOrderByRefIdAscStepAsc(Long boardId, Pageable pageable);

    @Modifying
    @Query("UPDATE CommentEntity SET step = step + 1 WHERE refId = :refId AND step >= :step")
    int updateComments(Long refId, Long step);

    Page<CommentEntity> findAllByUserIdOrderByWriteDateDesc(Long userId, Pageable pageable);

    Optional<CommentEntity> findTopByBoardIdAndRefIdAndDepthAndStepBetweenOrderByStepDesc (Long boardId, Long refId, Long depth, Long step, Long step2);

    Optional<CommentEntity> findTopByBoardIdAndStepGreaterThanAndDepthOrderByWriteDate(Long boardId, Long step, Long depth);

    @Query("""
    select NVL(min(c.step), (select max(c.step) + 1 from CommentEntity c where c.refId = :refId))
    from CommentEntity c
    where c.refId = :refId and c.step > :step and c.depth <= :depth
    """)
    Long getLastStep(Long refId, Long step, Long depth);

    Page<CommentEntity> findAllByBoardId(Long boardId, PageRequest pageRequest);
}
