package com.haoo.iframe.jwt.config;

import com.haoo.iframe.jwt.filter.JWTAuthenticationFilter;
import com.haoo.iframe.jwt.filter.JWTLoginFilter;
import com.haoo.iframe.jwt.service.LoginService;
import com.haoo.iframe.jwt.service.TokenAuthenticationService;
import com.haoo.iframe.service.account.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;

    private final TokenAuthenticationService tokenAuthenticationService;

    public WebSecurityConfig(AccountService accountService, TokenAuthenticationService tokenAuthenticationService) {
        this.accountService = accountService;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf验证
        http.csrf().disable()
                // 对请求进行认证
                .authorizeRequests()
                // 所有 / 的所有请求 都放行
                //.antMatchers("/").permitAll()
                // 所有 /login 的POST请求 都放行
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // swagger 资源过滤
                .antMatchers("/swagger**/**", "/webjars/**", "/v2/**", "/v3/**", "/doc.html").permitAll()
                // 权限检查/角色检查 hasAuthority(不加前缀) or hasRole(自定加前缀ROLE_)
//                .antMatchers("/t/test").hasAuthority("ROLE_ADMIN")
                // 所有请求需要身份认证
                .anyRequest().authenticated()
                // 开启登录
//                .and().formLogin().loginProcessingUrl("/login").permitAll()
                //开启注销、清除身份信息、session失效
//                .and().logout().logoutUrl("/logout").clearAuthentication(true).invalidateHttpSession(true);
                .and()
                // 添加一个过滤器 所有访问 /login 的请求交给 JWTLoginFilter 来处理 这个类处理所有的JWT相关内容
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(), tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class)
                // 添加一个过滤器验证其他请求的Token是否合法
                .addFilterBefore(new JWTAuthenticationFilter(tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件

        // 在UserDetailsService您只获得用户名时，当您返回自定义 UserDeatails 时，框架会检查密码
        // 但是在另一个系统中，我将密码存储在我的数据库中，所以我所要做的就是实现 UserDetailsService
        // 并检查用户是否存在于我的数据库中，剩下的事情就交给 spring-security 了。
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());

        // 在 AuthenticationProvider 中，可以检查用户名和密码，并在其中返回Authentication自定义对象。
        // 当您使用不同的身份验证系统，并且您自己的数据库/数据模型中未提供密码时，您必须使用 AuthenticationProvider。
        // 例如，我在一个项目中工作，客户有一个集中式身份验证系统（CAS），所以我的系统不知道密码，
        // 我必须实现 AuthenticationProvider 并将给定的密码发送给 CAS，然后获取它的返回结果在进行下一步。
//        auth.authenticationProvider(new LoginProvider(accountService));

//        auth.inMemoryAuthentication().withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles("ADMIN");
    }


    @Override
    protected UserDetailsService userDetailsService() {
        return new LoginService(accountService);
    }


    /*  Spring Security 默认登录需要实例化
        没有passwordEncoder会抛java.lang.IllegalArgumentException:
        There is no PasswordEncoder mapped for the id "null"
    */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
