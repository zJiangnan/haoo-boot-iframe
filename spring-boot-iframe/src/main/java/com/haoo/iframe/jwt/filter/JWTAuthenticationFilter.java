package com.haoo.iframe.jwt.filter;

import com.haoo.iframe.jwt.service.TokenAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

    private final TokenAuthenticationService tokenAuthenticationService;

    public JWTAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {

        // 登录认证
        Authentication authentication = tokenAuthenticationService
                .getAuthentication((HttpServletRequest) request);
        //当前登陆信息
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        //转发请求
        filterChain.doFilter(request, response);
    }
}
