package com.anitalk.app.animation;

import com.anitalk.app.animation.dto.AnimationRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/animations")
public class AnimationController {
    private final AnimationService animationService;

    @GetMapping
    public ResponseEntity<List<AnimationRecord>> getAnimations(){
        List<AnimationRecord> animations = animationService.getAnimations();
        return ResponseEntity.ok(animations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimationRecord> getAnimationsById(@PathVariable Long id){
        try {
            AnimationRecord animation = animationService.getAnimations(id);
            return ResponseEntity.ok(animation);
        } catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<AnimationRecord> addAnimations(@RequestBody AnimationRecord animationRecord){
        try {
            AnimationRecord addedAnimation = animationService.addAnimations(animationRecord);

            return ResponseEntity.ok(addedAnimation);
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimationRecord> putAnimations(@PathVariable Long id, @RequestBody AnimationRecord animationRecord){
        try {
            AnimationRecord animations = animationService.putAnimations(id, animationRecord);
            return ResponseEntity.ok(animations);
        } catch (NoSuchElementException e){
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
