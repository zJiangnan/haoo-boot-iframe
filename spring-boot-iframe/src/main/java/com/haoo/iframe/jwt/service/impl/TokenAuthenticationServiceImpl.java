package com.haoo.iframe.jwt.service.impl;

import com.haoo.iframe.common.enums.ApiCode;
import com.haoo.iframe.common.system.MD5;
import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.jwt.service.TokenAuthenticationService;
import com.haoo.iframe.service.account.AccountService;
import com.haoo.iframe.vo.UserVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    static final long EXPIRATIONTIME = 432_000_000;            // 5天
    static final String SECRET = MD5.getMD5String("p@ssWord"); // jwt密钥 必须大于32个字符
    static final String TOKEN_PREFIX = "Bearer";               // Token前缀
    static final String HEADER_STRING = "Authorization";       // 存放Token的Header Key

    private final AccountService accountService;

    public TokenAuthenticationServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void addAuthentication(HttpServletResponse response, Authentication auth) {
        // 获取认证的用户名
        String username = auth.getName();
        // 设置 权限（角色）
        String authorities = String.join(",", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        // 生成JWT
        String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", authorities)
                // 用户名写入标题
                .setSubject(username)
                // 有效期设置
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                // 签名设置
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        // 将 JWT 写入 body
        try {
            SecurityContextHolder.getContext()
                    .setAuthentication(auth);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(new ReturnEntity(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), JWT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        // 从Header中拿到token
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            // 解析 Token
            Claims claims = null;

            try {
                claims = Jwts.parserBuilder()
                        // 验签
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                        // 去掉 Bearer
                        .build().parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody();
            } catch (Exception e) {
                return null;
            }

            // 拿用户名
            String user = claims.getSubject();

            // 得到 权限（角色）
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));

            // 返回验证令牌
            return user != null ?
                    new UsernamePasswordAuthenticationToken(null, null, authorities) :
                    null;
        }
        return null;
    }

}
