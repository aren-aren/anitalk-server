package com.anitalk.app.domain.user;

import com.anitalk.app.domain.user.dto.*;
import com.anitalk.app.security.JwtGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtGenerator generator;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/users")
    public ResponseEntity<UserRecord> join(@RequestBody AuthenticateUserRecord authenticateUserRecord){
        UserRecord userRecord = userService.joinUser(authenticateUserRecord);
        return ResponseEntity.ok(userRecord);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenRecord> login(@RequestBody AuthenticateUserRecord authenticateUserRecord){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateUserRecord.email(), authenticateUserRecord.password()));
        AuthenticateUserRecord loginUser = ((LoginUser) authentication.getPrincipal()).getUserRecord();
        String token = generator.generateToken(loginUser.id(), loginUser.email());

        return ResponseEntity.ok(new UserTokenRecord(
                new UserRecord(loginUser.id(), loginUser.email(), loginUser.nickname()) ,
                new JwtToken("Bearer", token)
        ));
    }

    @PutMapping("/users")
    public ResponseEntity<UserRecord> changeNickname(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @RequestBody UserRecord userRecord ) throws Exception {
        if(user == null){
            throw new Exception("로그인이 필요합니다.");
        }

        UserRecord changedUserRecord = userService.changeNickname(user.id(), userRecord);
        return ResponseEntity.ok(changedUserRecord);
    }
}
