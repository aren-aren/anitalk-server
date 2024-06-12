package com.anitalk.app.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByBoardIdOrderByRefIdDescStepAsc(Long boardId);

    @Modifying
    @Query("UPDATE CommentEntity SET step = step + 1 WHERE refId = :refId AND step >= :step")
    int updateComments(Long refId, Long step);
}
