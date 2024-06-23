package com.anitalk.app.domain.animation;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.commons.StringResult;
import com.anitalk.app.domain.animation.dto.AnimationRecord;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/animations")
public class AnimationController {
    private final AnimationService animationService;

    @GetMapping
    public ResponseEntity<PageAnd<AnimationRecord>> getAnimations(Pagination pagination){
        PageAnd<AnimationRecord> animations = animationService.getAnimations(pagination);
        return ResponseEntity.ok(animations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimationRecord> getAnimationsById(@PathVariable Long id){
        AnimationRecord animation = animationService.getAnimations(id);
        return ResponseEntity.ok(animation);
    }

    @PostMapping
    public ResponseEntity<AnimationRecord> addAnimations(@RequestBody AnimationRecord animationRecord){
        AnimationRecord addedAnimation = animationService.addAnimations(animationRecord);
        return ResponseEntity.ok(addedAnimation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimationRecord> putAnimations(@PathVariable Long id, @RequestBody AnimationRecord animationRecord){
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
}
