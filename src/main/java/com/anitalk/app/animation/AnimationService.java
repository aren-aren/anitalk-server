package com.anitalk.app.animation;

import com.anitalk.app.animation.dto.AnimationRecord;
import com.anitalk.app.attach.AttachManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimationService {
    private final AnimationRepository repository;
    private final AttachManager attachManager;

    public List<AnimationRecord> getAnimations(){
        return repository.findAll().stream().map(AnimationRecord::of).toList();
    }

    public AnimationRecord getAnimations(Long id) {
        AnimationEntity animationEntity = repository.findById(id).orElseThrow();
        return AnimationRecord.of(animationEntity);
    }

    public AnimationRecord addAnimations(AnimationRecord animationRecord) {
        AnimationEntity addedEntity = repository.save(animationRecord.toEntity());
        if(animationRecord.attach() != null){
            attachManager.connectAttaches("animations", addedEntity.getId(), animationRecord.attach());
        }
        return AnimationRecord.of(addedEntity);
    }

    public AnimationRecord putAnimations(Long id, AnimationRecord animationRecord) {
        AnimationEntity entity = repository.findById(id).orElseThrow();
        animationRecord.putEntity(entity);
        entity.setId(id);

        AnimationEntity putAnimation = repository.save(entity);
        if(animationRecord.attach() != null){
            attachManager.PutConnectionAttaches("animations", putAnimation.getId(), animationRecord.attach());
        }
        return AnimationRecord.of(putAnimation);
    }
}
