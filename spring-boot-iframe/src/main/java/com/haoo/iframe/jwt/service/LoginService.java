package com.haoo.iframe.jwt.service;

import com.haoo.iframe.common.enums.ApiCode;
import com.haoo.iframe.common.system.AccountCredentials;
import com.haoo.iframe.jwt.service.impl.GrantedAuthorityImpl;
import com.haoo.iframe.service.account.AccountService;
import com.haoo.iframe.vo.UserVo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

public class LoginService implements UserDetailsService {

    private final AccountService accountService;

    public LoginService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据用户名，查找到对应的密码，与权限
        UserVo user = accountService.loginUser(username);

        //用户不存在
        if (ObjectUtils.isEmpty(user)) {
            // 前端  ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}
            throw new BadCredentialsException(ApiCode.CLIENT_B_USER_ACCOUNT_NOT_EXIST.getMessage());
        }

        // 这里设置权限和角色
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();

        // 系统管理员
        authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
        authorities.add(new GrantedAuthorityImpl("AUTH_ADMIN"));
        authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));

        return new AccountCredentials(user.getLoginName(), user.getPassword(), authorities);
    }


}
