package com.anitalk.app.domain.user;

import com.anitalk.app.domain.user.dto.*;
import com.anitalk.app.exception.UserTokenException;
import com.anitalk.app.security.JwtGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserLoginService userLoginService;
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
        String token = generator.generateAccessToken(loginUser.id(), loginUser.email());
        String refreshToken = generator.generateRefreshToken(loginUser.id(), loginUser.email());

        JwtToken jwtToken = userLoginService.setRefreshToken(loginUser.id(), refreshToken);


        return ResponseEntity.ok()
                .header("Set-Cookie", generator.generateRefreshCookie(jwtToken.toString()).toString())
                .body(new UserTokenRecord(
                        new UserRecord(loginUser.id(), loginUser.email(), loginUser.nickname()),
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

    @PutMapping("/users/password")
    public ResponseEntity<UserRecord> changePassword(
            @AuthenticationPrincipal AuthenticateUserRecord user,
            @RequestBody AuthenticateUserRecord userRecord ) throws Exception {
        if(user == null){
            throw new Exception("로그인이 필요합니다.");
        }

        UserRecord changedUserRecord = userService.changePassword(user.id(), userRecord);
        return ResponseEntity.ok(changedUserRecord);
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<EmailDuplicationRecord> checkEmail(@PathVariable String email) throws Exception {
        if(!userService.emailValidate(email)){
            throw new Exception("이메일 형식이 맞지 않습니다.");
        }

        EmailDuplicationRecord result = userService.checkEmail(email);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserTokenRecord> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) throws Exception {
        System.out.println("refreshToken = " + refreshToken);
        if(generator.validateToken(refreshToken)){
            Map<String, Object> claims = generator.getClaimsFromToken(refreshToken);

            UserRecord userRecord = userLoginService.validateRefreshToken(Long.parseLong(claims.get("userId").toString()), refreshToken);

            String token = generator.generateAccessToken(userRecord.id(), userRecord.email());
            return ResponseEntity.ok(new UserTokenRecord(userRecord, new JwtToken("Bearer", token)));
        }

        throw new UserTokenException("refreshToken_expired");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal AuthenticateUserRecord userRecord,
            @CookieValue(name = "refreshToken") String refreshToken
    ) throws Exception {
        if(userRecord == null && refreshToken == null) throw new Exception("로그인이 되어있지 않습니다.");

        Long userId;

        if(userRecord == null){
            Map<String, Object> claims = generator.getClaimsFromToken(refreshToken);
            userId = Long.parseLong(claims.get("userId").toString());
        } else {
            userId = userRecord.id();
        }

        userLoginService.logoutUser(userId);
        return ResponseEntity.ok().build();
    }
}
