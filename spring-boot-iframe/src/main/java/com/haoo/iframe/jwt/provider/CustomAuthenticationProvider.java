package com.haoo.iframe.jwt.provider;

import com.haoo.iframe.common.enums.ApiCode;
import com.haoo.iframe.common.system.MD5;
import com.haoo.iframe.jwt.service.impl.GrantedAuthorityImpl;
import com.haoo.iframe.service.account.AccountService;
import com.haoo.iframe.vo.UserVo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

// 自定义身份认证验证组件
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AccountService accountService;

    public CustomAuthenticationProvider(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserVo user = accountService.loginUser(name);
        //用户不存在
        if (ObjectUtils.isEmpty(user)){
            throw new BadCredentialsException(ApiCode.CLIENT_B_USER_ACCOUNT_NOT_EXIST.getMessage());
        }

        // 认证逻辑
        if (MD5.getMD5String(password).equals(user.getPassword())) {
            // 这里设置权限和角色
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();

            // 系统管理员
            authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
            authorities.add(new GrantedAuthorityImpl("AUTH_ADMIN"));
            authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));
            // 管理员
            //authorities.add(new GrantedAuthorityImpl("AUTH_ADMIN"));
            //authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));
            // 普通用户
            //authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));

            // 生成令牌
            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, authorities);
            return auth;

        } else {
            throw new BadCredentialsException(ApiCode.CLIENT_B_USER_PASSWORD_ERROR.getMessage());
        }

    }

    // 是否可以提供输入类型的认证服务
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

