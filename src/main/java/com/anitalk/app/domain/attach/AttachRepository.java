package com.anitalk.app.domain.attach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface AttachRepository extends JpaRepository<AttachEntity, Long> {
    List<AttachEntity> findAllByNameIn(Collection<String> name);
    List<AttachEntity> findAllByCategoryAndParentIdOrNameIn(String category, Long parentId, Collection<String> name);
    AttachEntity findByParentId(Long parentId);

    Set<AttachEntity> findAllByCategoryAndParentIdIn(String category, List<Long> parentId);

    AttachEntity findByCategoryAndParentId(String category, Long parentId);
}
