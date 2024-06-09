package com.anitalk.app.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findAllByAnimationId(Long animationId);
    Optional<BoardEntity> findByIdAndAnimationId(Long id, Long animationId);
    Optional<BoardEntity> findByAnimationId(Long animationId);
}
