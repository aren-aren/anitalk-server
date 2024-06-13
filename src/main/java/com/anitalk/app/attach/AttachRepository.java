package com.anitalk.app.attach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AttachRepository extends JpaRepository<AttachEntity, Long> {
    List<AttachEntity> findAllByNameIn(Collection<String> name);
    List<AttachEntity> findAllByCategoryAndParentIdOrNameIn(String category, Long parentId, Collection<String> name);
    AttachEntity findByParentId(Long parentId);
}
