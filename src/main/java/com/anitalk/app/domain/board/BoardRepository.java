package com.anitalk.app.domain.board;

import com.anitalk.app.utils.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findAllByAnimationIdAndDeletedIsFalse(Long animationId, Pageable pagination);
    Optional<BoardEntity> findByIdAndAnimationIdAndDeletedIsFalse(Long id, Long animationId);
    Optional<BoardEntity> findByAnimationIdAndDeletedIsFalse(Long animationId);

    Page<BoardEntity> findAllByUserIdAndDeletedIsFalse(Long userId, Pageable pageable);

    Optional<BoardEntity> findByUserIdAndAnimationIdAndIdAndDeletedIsFalse(Long userId, Long animationId, Long id);

    Page<BoardEntity> findAllByDeleted(Boolean deleted, Pageable pageable);

    Optional<BoardEntity> findByNicknameAndPasswordAndAnimationIdAndIdAndDeletedIsFalse(String nickname, String password, Long animationId, Long boardId);

}
