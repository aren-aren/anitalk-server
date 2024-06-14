package com.anitalk.app.domain.animation;

import com.anitalk.app.commons.PageAnd;
import com.anitalk.app.domain.animation.dto.AnimationRecord;
import com.anitalk.app.utils.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/animations")
public class AnimationController {
    private final AnimationService animationService;

    @GetMapping
    public ResponseEntity<PageAnd<AnimationRecord>> getAnimations(Pagination pagination){
        PageAnd<AnimationRecord> animations = new PageAnd<>(animationService.getAnimations(pagination));
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
}