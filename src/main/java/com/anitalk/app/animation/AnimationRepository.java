package com.anitalk.app.animation;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimationRepository extends JpaRepository<AnimationEntity, Long> {

    AnimationEntity findByName(String name);
}
