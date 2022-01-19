package com.haoo.iframe.util;

import lombok.Data;

@Data
public class ResultMessage<T> {
    private Integer code;
    private String message;
    private T data;

    public ResultMessage(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
