package com.anitalk.app.security;

import com.anitalk.app.commons.ResultResponse;
import com.anitalk.app.domain.user.UserLoginService;
import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
import com.anitalk.app.domain.user.dto.UserRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    private final JwtGenerator jwtGenerator;
    private final UserLoginService userLoginService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getJWTFromRequest(request);
        ResultResponse resultResponse = createResultResponse(token, request);

        request.setAttribute("resultResponse", resultResponse);

        filterChain.doFilter(request, response);
    }

    private ResultResponse createResultResponse(String token, HttpServletRequest request){
        /* 익명 사용자 */
        if (!StringUtils.hasText(token)) {
            return new ResultResponse("anonymous");
        }

        /* 로그인 한 사용자 (accessToken) */
        if(jwtGenerator.validateToken(token)){
            Map<String, Object> claims = jwtGenerator.getClaimsFromToken(token);

            String userId = claims.get("userId").toString();
            String email = claims.get("email").toString();

            if(userId == null || email == null){
                return new ResultResponse("invalidate");
            }

            authenticateUser(getUserDetail(userId, email), request);
            return new ResultResponse("authenticate");
        }

        String refreshToken = getRefreshTokenInCookie(request.getCookies());
        /* accessToken, refreshToken 만료 */
        if(refreshToken == null || !jwtGenerator.validateToken(refreshToken)) return new ResultResponse("expired");

        /* refreshToken 으로 accessToken 재발급 */
        ResultResponse resultResponse = new ResultResponse("refresh");
        Map<String, Object> claims = jwtGenerator.getClaimsFromToken(refreshToken);

        String userId = claims.get("userId").toString();
        try {
            UserRecord record = userLoginService.validateRefreshToken(Long.valueOf(userId), refreshToken);
            String accessToken = jwtGenerator.generateAccessToken(record.id(), record.email());

            authenticateUser(getUserDetail(String.valueOf(record.id()), record.email()), request);

            resultResponse.setAccessToken(accessToken);
        } catch (Exception e){
            return new ResultResponse("invalidate");
        }

        return resultResponse;
    }

    private void authenticateUser(AuthenticateUserRecord userRecord, HttpServletRequest request){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userRecord, null, null);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String getRefreshTokenInCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("refreshToken")){
                return cookie.getValue();
            }
        }
        return null;
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private AuthenticateUserRecord getUserDetail(String userId, String email){
        return new AuthenticateUserRecord(Long.parseLong(userId), email, null, null);
    }

    private String refreshToken(String refreshToken) throws Exception {
        if(!jwtGenerator.validateToken(refreshToken)){
            throw new Exception("refresh Token expired");
        }
        return refreshToken;
    }
}
