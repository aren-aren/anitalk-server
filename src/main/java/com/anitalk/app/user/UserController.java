package com.anitalk.app.user;

import com.anitalk.app.user.dto.JoinUserRecord;
import com.anitalk.app.user.dto.UserRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserRecord> join(@RequestBody JoinUserRecord joinUserRecord){
        System.out.println(joinUserRecord);
        UserRecord userRecord = userService.joinUser(joinUserRecord);
        return ResponseEntity.ok(userRecord);
    }
}
