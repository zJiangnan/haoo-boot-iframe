package com.haoo.iframe.common.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class AccountCredentials implements UserDetails {

    private String username;
    private String password;
    private boolean enabled = true;
    private List<GrantedAuthority> authorities;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {  // 帐户是否过期
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {   // 帐户是否被冻结
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {  // 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
        return true;
    }

    @Override
    public boolean isEnabled() {    // 帐号是否可用
        return enabled;
    }


    public AccountCredentials(String username, String password, List<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public AccountCredentials(String username, String password, List<GrantedAuthority> authorities, boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public AccountCredentials() {}
}
