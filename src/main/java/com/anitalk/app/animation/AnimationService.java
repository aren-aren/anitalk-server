package com.anitalk.app.animation;

import com.anitalk.app.animation.dto.AnimationRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimationService {
    private final AnimationRepository repository;

    public List<AnimationRecord> getAnimations(){
        return repository.findAll().stream().map(AnimationRecord::of).toList();
    }

    public AnimationRecord getAnimations(Long id) {
        AnimationEntity animationEntity = repository.findById(id).orElseThrow();
        return AnimationRecord.of(animationEntity);
    }

    public AnimationRecord addAnimations(AnimationRecord animationRecord) {
        AnimationEntity addedEntity = repository.save(animationRecord.toEntity());
        return AnimationRecord.of(addedEntity);
    }

    public AnimationRecord putAnimations(Long id, AnimationRecord animationRecord) {
        AnimationEntity entity = repository.findById(id).orElseThrow();
        animationRecord.putEntity(entity);
        entity.setId(id);

        AnimationEntity putAnimation = repository.save(entity);
        return AnimationRecord.of(putAnimation);
    }
}
