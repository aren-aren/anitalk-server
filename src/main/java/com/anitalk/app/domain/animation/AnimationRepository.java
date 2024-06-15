package com.anitalk.app.domain.animation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimationRepository extends JpaRepository<AnimationEntity, Long> {
    AnimationEntity findByName(String name);
}
