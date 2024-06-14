package com.anitalk.app.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findAllByAnimationId(Long animationId, Pageable pagination);
    Optional<BoardEntity> findByIdAndAnimationId(Long id, Long animationId);
    Optional<BoardEntity> findByAnimationId(Long animationId);
}
