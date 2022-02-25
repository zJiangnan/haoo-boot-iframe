package com.haoo.iframe.jwt.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenAuthenticationService {
    /**
     * JWT生成方法
     * @param response
     * @param auth
     */
    void addAuthentication(HttpServletResponse response, Authentication auth);

    /***
     * JWT验证方法
     * @param request
     * @return
     */
    Authentication getAuthentication(HttpServletRequest request);
}
