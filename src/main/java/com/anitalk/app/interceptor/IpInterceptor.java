package com.anitalk.app.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class IpInterceptor implements HandlerInterceptor {
    private String[] headers = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = null;
        for(String headerType: headers) {
            ip = request.getHeader(headerType);
            if(ip != null) break;
        }

        // 적용
        if (ip == null) ip = request.getRemoteAddr();
        if (ip == null) ip = "unknown";

        request.setAttribute("ip", ip);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
