package com.anitalk.app.security;

import com.anitalk.app.domain.user.dto.AuthenticateUserRecord;
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

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    private final JwtGenerator jwtGenerator;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = getJWTFromRequest(request);

        if (StringUtils.hasText(token)) {
            if(jwtGenerator.validateToken(token)){
                Map<String, Object> claims = jwtGenerator.getClaimsFromToken(token);

                AuthenticateUserRecord userRecord = getUserDetail(claims.get("userId").toString(), claims.get("email").toString());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userRecord, null, null);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                response.setStatus(401);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.print("accessToken expired");
                outputStream.flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
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
}
