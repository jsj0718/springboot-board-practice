package com.elevenhelevenm.practice.board.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터 체인에 등록
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

/*
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
*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login"); //login 실패 시 해당 url로 리다이렉트
    }
}
