package com.haoo.iframe.common.exception;

import com.haoo.iframe.common.constant.ApiCode;

/**
 * {@code BizException} 接管项目抛出的异常信息
 * @Package: cn.echo.enterprise.errcode
 * @Author: pluto
 * @CreateTime: 2021/10/26 3:44 下午
 * @Description: 标准业务异常基类
 * @since 1.0
 **/
public class BizException extends RuntimeException {

    /**
     * 异常code码
     **/
    private Integer code;

    /**
     * 系统id
     */
    private String systemId;

    public BizException(Integer code, String message) {
        super(buildMessage(code, message));
        this.code = code;
    }

    public BizException(ApiCode apiCode) {
        super(buildMessage(apiCode.getCode(), apiCode.getMessage()));
        this.code = apiCode.getCode();
    }

    public BizException(Integer code, String systemId, String message) {
        super(buildMessage(code, systemId, message));
        this.code = code;
        this.systemId = systemId;
    }

    public BizException(ApiCode apiCode, String systemId) {
        super(buildMessage(apiCode.getCode(), systemId, apiCode.getMessage()));
        this.code = apiCode.getCode();
        this.systemId = systemId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    private static String buildMessage(Integer errorCode, String message) {
        return buildMessage(errorCode, null, message);
    }

    private static String buildMessage(Integer errorCode, String systemId, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("'code': " + errorCode);
        builder.append(",");
        builder.append(String.format("'systemId': '%s'", systemId));
        builder.append(",");
        builder.append(String.format("'message': '%s'", message));
        builder.append("}");

        return builder.toString();
    }

}
