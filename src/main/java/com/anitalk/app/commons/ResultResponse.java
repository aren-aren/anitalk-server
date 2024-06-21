package com.anitalk.app.commons;

import lombok.Data;

@Data
public class ResultResponse {
    String result;
    Object data;
    String accessToken;

    public ResultResponse(String result) {
        this.result = result;
    }
}
