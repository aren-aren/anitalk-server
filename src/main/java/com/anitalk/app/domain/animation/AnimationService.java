package com.anitalk.app.domain.animation;

import com.anitalk.app.domain.animation.dto.AnimationRecord;
import com.anitalk.app.domain.attach.AttachEntity;
import com.anitalk.app.domain.attach.AttachManager;
import com.anitalk.app.domain.attach.AttachRepository;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimationService {
    private final AnimationRepository animationRepository;
    private final AttachRepository attachRepository;
    private final AttachManager attachManager;

    @Value("${aws.url}")
    private String url;
    private final String CATEGORY = "animations";

    public Page<AnimationRecord> getAnimations(Pagination page){
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<AnimationEntity> animations = animationRepository.findAll(pageable);
        List<Long> ids = animations.map(AnimationEntity::getId).toList();
        Map<Long, AttachEntity> attaches =
                attachRepository.findAllByCategoryAndParentIdIn(CATEGORY, ids).stream()
                        .collect(Collectors.toMap(AttachEntity::getParentId, attachEntity->attachEntity));

        return animations.map(animation -> {
            AttachEntity attach = attaches.get(animation.getId());
            String thumbnailUrl = attach == null ? null : url + attach.getName();
            return AnimationRecord.of(animation, thumbnailUrl);
        });
    }

    public AnimationRecord getAnimations(Long id) {
        AnimationEntity animationEntity = animationRepository.findById(id).orElseThrow();
        AttachEntity attachEntity = attachRepository.findByCategoryAndParentId(CATEGORY, animationEntity.getId());

        return AnimationRecord.of(animationEntity, url + attachEntity.getName());
    }

    public AnimationRecord addAnimations(AnimationRecord animationRecord) {
        AnimationEntity addedEntity = animationRepository.save(animationRecord.toEntity());
        if(animationRecord.attach() != null){
            attachManager.connectAttaches("animations", addedEntity.getId(), animationRecord.attach());
        }
        return AnimationRecord.of(addedEntity, url);
    }

    public AnimationRecord putAnimations(Long id, AnimationRecord animationRecord) {
        AnimationEntity entity = animationRepository.findById(id).orElseThrow();
        animationRecord.putEntity(entity);
        entity.setId(id);

        AnimationEntity putAnimation = animationRepository.save(entity);
        if(animationRecord.attach() != null){
            attachManager.PutConnectionAttaches("animations", putAnimation.getId(), animationRecord.attach());
        }
        return AnimationRecord.of(putAnimation, url);
    }
}
