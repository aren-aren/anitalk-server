package com.anitalk.app.domain.attach;

import com.anitalk.app.utils.DateManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttachManagerTest {

    @Autowired
    AttachRepository repository;

    @Test
    public void test(){
        List<AttachEntity> animations = repository.findAllToDeleteScheduling("animations", DateManager.getDate(-7));
        assertNotEquals(animations.size() , 0);
        assertEquals(animations.size(), 1);

        System.out.println(animations);
    }
}
