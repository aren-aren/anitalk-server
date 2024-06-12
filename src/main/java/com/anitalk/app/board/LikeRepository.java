package com.anitalk.app.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, LikeEntityId> {
}
