package com.anitalk.app.responseAdvice;

import com.anitalk.app.commons.ResultResponse;
import com.anitalk.app.domain.user.dto.UserTokenRecord;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.anitalk.app")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        System.out.println("request.getURI() = " + request.getURI().getPath());

        try {
            ResultResponse resultResponse
                    = (ResultResponse) RequestContextHolder.getRequestAttributes().getAttribute("resultResponse", 0);

            if(request.getURI().getPath().equals("/api/login")){
                UserTokenRecord tokenRecord = (UserTokenRecord) body;
                resultResponse.setResult("authenticate");
                resultResponse.setAccessToken(tokenRecord.token().token());
            }

            resultResponse.setData(body);

            return resultResponse;
        } catch (Exception e){
            return body;
        }
    }
}
