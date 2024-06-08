package com.anitalk.app.animation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/animations/")
public class AnimationController {
    private final AnimationRepository repository;

    @GetMapping("")
    public ResponseEntity<List<AnimationEntity>> getAnimations(){
        List<AnimationEntity> animations = repository.findAll();
        return ResponseEntity.ok(animations);
    }
}
