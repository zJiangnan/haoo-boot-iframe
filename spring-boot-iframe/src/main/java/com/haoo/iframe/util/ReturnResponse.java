package com.haoo.iframe.util;

import lombok.Data;

@Data
public class ReturnResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public ReturnResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ReturnResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
