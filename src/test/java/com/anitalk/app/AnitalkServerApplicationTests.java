package com.anitalk.app;

import com.anitalk.app.domain.animation.AnimationService;
import com.anitalk.app.commons.Pagination;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnitalkServerApplicationTests {

    @Autowired
    AnimationService service;

    @Test
    void contextLoads() {
    }
}
