package com.elevenhelevenm.practice.board.config.jwt;

public interface JwtProperties {
    String SECRET = "secret";
    int EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
