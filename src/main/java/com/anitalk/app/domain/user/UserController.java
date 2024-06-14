package com.anitalk.app.domain.user;

import com.anitalk.app.domain.user.dto.*;
import com.anitalk.app.security.JwtGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println(authenticateUserRecord);
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
                new JwtToken("bearer", token)
        ));
    }
}
