package com.anitalk.app.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Page<ReviewEntity> findAllByAnimationId(Long animationId, Pageable pageable);

    Optional<ReviewEntity> findByIdAndUserId(Long id, Long userId);

    Optional<ReviewEntity> findByAnimationIdAndUserId(Long aLong, Long aLong1);
}
