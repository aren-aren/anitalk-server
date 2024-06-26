package com.anitalk.app.domain.animation;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.animation.dto.AnimationPutRecord;
import com.anitalk.app.domain.animation.dto.AnimationRecord;
import com.anitalk.app.domain.animation.dto.RankingOption;
import com.anitalk.app.domain.attach.AttachEntity;
import com.anitalk.app.domain.attach.AttachManager;
import com.anitalk.app.domain.attach.AttachRepository;
import com.anitalk.app.domain.user.UserEntity;
import com.anitalk.app.domain.user.UserRepository;
import com.anitalk.app.utils.DateManager;
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
    private final FavoriteRepository favoriteRepository;
    private final AttachRepository attachRepository;
    private final AttachManager attachManager;
    private final UserRepository userRepository;

    @Value("${aws.url}")
    private String url;
    private final String CATEGORY = "animations";

    public PageAnd<AnimationRecord> getAnimations(Long userId, Pagination page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
        Page<AnimationEntity> animations = animationRepository.findAll(pageable);

        return new PageAnd<>(getListWithThumbnail(animations, userId));
    }

    public AnimationRecord getAnimations(Long id, Long userId) {
        AnimationEntity animationEntity = animationRepository.findById(id).orElseThrow();
        AttachEntity attachEntity = attachRepository.findByCategoryAndParentId(CATEGORY, animationEntity.getId());

        return AnimationRecord.of(animationEntity, url + attachEntity.getName(), userId);
    }

    public AnimationRecord addAnimations(AnimationPutRecord animationRecord) {
        AnimationEntity addedEntity = animationRepository.save(animationRecord.toEntity());
        if (animationRecord.attach() != null) {
            attachManager.connectAttaches("animations", addedEntity.getId(), animationRecord.attach());
        }
        return AnimationRecord.of(addedEntity, url);
    }

    public AnimationRecord putAnimations(Long id, AnimationPutRecord animationRecord) {
        AnimationEntity entity = animationRepository.findById(id).orElseThrow();
        animationRecord.putEntity(entity);
        entity.setId(id);

        AnimationEntity putAnimation = animationRepository.save(entity);
        if (animationRecord.attach() != null) {
            attachManager.PutConnectionAttaches("animations", putAnimation.getId(), animationRecord.attach());
        }
        return AnimationRecord.of(putAnimation, url);
    }

    public void favoriteAnimation(Long userId, Long animationId) {
        if (favoriteRepository.findById(new FavoriteEntityId(userId, animationId)).isPresent()) return;

        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setId(new FavoriteEntityId(userId, animationId));

        UserEntity user = userRepository.findById(userId).orElseThrow();
        favoriteEntity.setUser(user);

        AnimationEntity animation = animationRepository.findById(animationId).orElseThrow();
        favoriteEntity.setAnimation(animation);

        favoriteRepository.save(favoriteEntity);
    }

    public void notFavoriteAnimations(Long id, Long animationId) {
        FavoriteEntity favoriteEntity = favoriteRepository.findById(new FavoriteEntityId(id, animationId)).orElseThrow();
        favoriteRepository.delete(favoriteEntity);
    }

    public PageAnd<AnimationRecord> getFavorites(Long userId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
        Page<AnimationEntity> animations = animationRepository.findAllByUserId(userId, pageable);
        return new PageAnd<>(getListWithThumbnail(animations, userId));
    }

    private Page<AnimationRecord> getListWithThumbnail(Page<AnimationEntity> animations, Long userId) {
        if (animations.getSize() == 0) return animations.map(animation -> AnimationRecord.of(animation, null, userId));

        List<Long> ids = animations.map(AnimationEntity::getId).toList();
        Map<Long, AttachEntity> attaches =
                attachRepository.findAllByCategoryAndParentIdIn(CATEGORY, ids).stream()
                        .collect(Collectors.toMap(AttachEntity::getParentId, attachEntity -> attachEntity));

        return animations.map(animation -> {
            AttachEntity attach = attaches.get(animation.getId());
            String thumbnailUrl = attach == null ? null : url + attach.getName();
            return AnimationRecord.of(animation, thumbnailUrl, userId);
        });
    }

    public PageAnd<AnimationRecord> getAnimations(RankingOption rankingOption, Pagination pagination, Long userId) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
        Page<AnimationEntity> animations = switch (rankingOption.rankBy()) {
            case HOT -> animationRepository.findAllHotRanking(pageable, DateManager.getDate(-30));
            case RATE -> animationRepository.findAllRateRanking(pageable);
        };

        return new PageAnd<>(getListWithThumbnail(animations, userId));
    }
}
