package com.elevenhelevenm.practice.board.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                    .and()
                .authorizeRequests()
                    .antMatchers("/", "/css/**", "/js/**", "/images/**", "/h2-console/**", "/join/**").permitAll() // 해당 URL은 인증 필요 X
                    .antMatchers("/api/**").hasRole("USER") // /api/** 는 USER 권한만 접근 가능
                    .anyRequest().authenticated(); //이외 모든 요청은 인증을 거친다.

        http
                .formLogin() //폼 로그인 기반
                    .and()
                .httpBasic(); //HTTP 로그인 기반
    }

}
