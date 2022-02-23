package com.haoo.iframe.common.system;

import lombok.Data;

@Data
public class ReturnEntity<T> {
    private Integer code;
    private String message;
    private T data;

    public ReturnEntity(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ReturnEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
