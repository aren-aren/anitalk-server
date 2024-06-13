package com.anitalk.app;

import com.anitalk.app.animation.AnimationService;
import com.anitalk.app.animation.dto.AnimationRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnitalkServerApplicationTests {
    @Autowired
    private AnimationService service;

    @Test
    void contextLoads() {
    }

    @Test
    void test(){
        AnimationRecord animation = service.getAnimations(3L);
        System.out.println(animation);
    }
}
