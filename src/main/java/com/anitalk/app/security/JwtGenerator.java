package com.anitalk.app.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtGenerator {
    private final Key key;

    public JwtGenerator(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateRefreshToken(Long userId, String username){
        Long expireTimeRefresh = 1000L * 60 * 60 * 24 * 7;
        return generateToken(userId, username, expireTimeRefresh);
    }

    public String generateAccessToken(Long userId, String username){
        Long expireTime = 1000L * 60 * 60;
        return generateToken(userId, username, expireTime);
    }

    public String generateToken(Long userId, String username, Long expireTime){
        Date current = new Date();
        Date expire = new Date(current.getTime() + expireTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(current)
                .setExpiration(expire)
                .signWith(key)
                .compact();
    }

    public Map<String, Object> getClaimsFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            log.error("token not valid : {}", e.getMessage());
            return false;
        }
    }

    public ResponseCookie generateRefreshCookie(String refreshToken){
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .build();
    }
}
