package com.anitalk.app.user;

import com.anitalk.app.user.dto.AuthenticateUserRecord;
import com.anitalk.app.user.dto.JwtRecord;
import com.anitalk.app.user.dto.UserRecord;
import com.anitalk.app.security.JwtGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<JwtRecord> login(@RequestBody AuthenticateUserRecord authenticateUserRecord){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateUserRecord.email(), authenticateUserRecord.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = generator.generateToken(authentication.getName());
        return ResponseEntity.ok(new JwtRecord(token));
    }
}
