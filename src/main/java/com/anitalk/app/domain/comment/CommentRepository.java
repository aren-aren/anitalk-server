package com.anitalk.app.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByBoardIdOrderByRefIdDescStepAsc(Long boardId, Pageable pageable);

    @Modifying
    @Query("UPDATE CommentEntity SET step = step + 1 WHERE refId = :refId AND step >= :step")
    int updateComments(Long refId, Long step);
}
