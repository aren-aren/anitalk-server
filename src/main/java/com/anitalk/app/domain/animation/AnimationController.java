package com.anitalk.app.domain.animation;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.commons.StringResult;
import com.anitalk.app.domain.animation.dto.AnimationSearchRecord;
import com.anitalk.app.domain.animation.dto.AnimationPutRecord;
import com.anitalk.app.domain.animation.dto.AnimationRecord;
import com.anitalk.app.domain.animation.dto.RankingOption;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/animations")
public class AnimationController {
    private final AnimationService animationService;

    @GetMapping
    public ResponseEntity<PageAnd<AnimationRecord>> getAnimations(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            AnimationSearchRecord searchRecord,
            Pagination pagination
    ){
        Long userId = null;
        if(user != null) userId = user.id();

        PageAnd<AnimationRecord> animations = animationService.getAnimations(userId, searchRecord, pagination);
        return ResponseEntity.ok(animations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimationRecord> getAnimationsById(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long id){
        Long userId = null;
        if(user != null) userId = user.id();
        AnimationRecord animation = animationService.getAnimations(id, userId);
        return ResponseEntity.ok(animation);
    }

    @PostMapping
    public ResponseEntity<AnimationRecord> addAnimations(@RequestBody AnimationPutRecord animationRecord){
        AnimationRecord addedAnimation = animationService.addAnimations(animationRecord);
        return ResponseEntity.ok(addedAnimation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimationRecord> putAnimations(@PathVariable Long id, @RequestBody AnimationPutRecord animationRecord){
        AnimationRecord animations = animationService.putAnimations(id, animationRecord);
        return ResponseEntity.ok(animations);
    }

    @PostMapping("/{animationId}/favorite")
    public ResponseEntity<StringResult> favoriteAnimations(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId
    ){
        animationService.favoriteAnimation(user.id(), animationId);
        return ResponseEntity.ok(new StringResult("success"));
    }

    @DeleteMapping("/{animationId}/favorite")
    public ResponseEntity<StringResult> notFavoriteAnimations(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @PathVariable Long animationId
    ){
        animationService.notFavoriteAnimations(user.id(), animationId);
        return ResponseEntity.ok(new StringResult("success"));
    }

    @GetMapping("/users")
    public ResponseEntity<PageAnd<AnimationRecord>> getFavorites(@AuthenticationPrincipal AuthenticateUserRecord user, Pagination pagination){
        PageAnd<AnimationRecord> animationRecords = animationService.getFavorites(user.id(), pagination);
        return ResponseEntity.ok(animationRecords);
    }

    @GetMapping("/ranking")
    public ResponseEntity<PageAnd<AnimationRecord>> getAnimationsRanking(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            RankingOption rankingOption,
            Pagination pagination){
        Long userId = null;
        if(user != null) userId = user.id();
        PageAnd<AnimationRecord> animationRecords = animationService.getAnimations(rankingOption, pagination, userId);
        return ResponseEntity.ok(animationRecords);
    }
}
