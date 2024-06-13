package com.anitalk.app.animation;

import com.anitalk.app.animation.dto.AnimationRecord;
import com.anitalk.app.attach.AttachManager;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimationService {
    private final AnimationRepository repository;
    private final AttachManager attachManager;

    @Value("${aws.url}")
    private String url;

    public List<AnimationRecord> getAnimations(Pagination page){
        Pageable pageable = PageRequest.of(page.getPage(), page.getPerPage());
        return repository.findAll(pageable).stream().map(animation -> AnimationRecord.of(animation, url)).toList();
    }

    public AnimationRecord getAnimations(Long id) {
        AnimationEntity animationEntity = repository.findById(id).orElseThrow();
        return AnimationRecord.of(animationEntity, url);
    }

    public AnimationRecord addAnimations(AnimationRecord animationRecord) {
        AnimationEntity addedEntity = repository.save(animationRecord.toEntity());
        if(animationRecord.attach() != null){
            attachManager.connectAttaches("animations", addedEntity.getId(), animationRecord.attach());
        }
        return AnimationRecord.of(addedEntity, url);
    }

    public AnimationRecord putAnimations(Long id, AnimationRecord animationRecord) {
        AnimationEntity entity = repository.findById(id).orElseThrow();
        animationRecord.putEntity(entity);
        entity.setId(id);

        AnimationEntity putAnimation = repository.save(entity);
        if(animationRecord.attach() != null){
            attachManager.PutConnectionAttaches("animations", putAnimation.getId(), animationRecord.attach());
        }
        return AnimationRecord.of(putAnimation, url);
    }
}
